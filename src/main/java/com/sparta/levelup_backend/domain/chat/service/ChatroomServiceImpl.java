package com.sparta.levelup_backend.domain.chat.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sparta.levelup_backend.domain.chat.document.ChatroomDocument;
import com.sparta.levelup_backend.domain.chat.document.Participant;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomCreateResponseDto;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomListResponseDto;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomMongoRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.BadRequestException;
import com.sparta.levelup_backend.exception.common.DuplicateException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {

	private final ChatroomMongoRepository chatroomMongoRepository;
	private final UserRepository userRepository;

	@Override
	public ChatroomCreateResponseDto createChatroom(Long userId, Long targetUserId, String title) {

		// 채팅방 생성자과 상대가 같은지 확인
		if (userId.equals(targetUserId)) {
			throw new BadRequestException(INVALID_CHATROOM_CREATE);
		}

		// 상대와의 채팅방이 이미 존재하는 지 확인
		if (chatroomMongoRepository.countByParticipantsUserIds(Arrays.asList(targetUserId, userId)) > 0) {
			throw new BadRequestException(DUPLICATE_CHATROOM);
		}

		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		UserEntity targetUser = userRepository.findByIdOrElseThrow(targetUserId);

		// 제목을 적지 않았을 경우 참여자 닉네임으로 자동 생성
		String chatroomTitle = StringUtils.hasText(title)
			? title
			: user.getNickName() + ", " + targetUser.getNickName();

		// 안 읽은 메시지값 기본값 0 으로 설정
		Map<String, Integer> unreadMessages = new HashMap<>();
		unreadMessages.put(userId.toString(), 0);
		unreadMessages.put(targetUserId.toString(), 0);

		Participant participant = new Participant(user);
		Participant participant1 = new Participant(targetUser);

		ChatroomDocument chatroom = ChatroomDocument.builder()
			.title(chatroomTitle)
			.participants(Arrays.asList(participant, participant1))
			.lastMessage("")
			.unreadMessages(unreadMessages)
			.build();

		ChatroomDocument savedChatroom = chatroomMongoRepository.save(chatroom);

		return ChatroomCreateResponseDto.builder()
			.chatroomId(savedChatroom.getId())
			.title(chatroomTitle)
			.participants(savedChatroom.getParticipants())
			.build();
	}

	@Transactional
	@Override
	public void leaveChatroom(Long userId, String chatroomId) {
		ChatroomDocument chatroom = chatroomMongoRepository.findByIdOrThrow(chatroomId);

		// 채팅방에 참여자로 존재하는 지 확인
		boolean isParticipant = chatroom.getParticipants().stream()
			.anyMatch(user -> user.getUserId().equals(userId));

		if(!isParticipant) {
			throw new DuplicateException(PARTICIPANT_ISDELETED);
		}

		// 채팅방 참여자 목록 업데이트
		List<Participant> participants = chatroom.getParticipants().stream()
			.filter(user -> !user.getUserId().equals(userId))
			.collect(Collectors.toList());
		chatroom.getUnreadMessages().remove(userId,toString());
		chatroom.updateParticipants(participants);

		// 채팅방에 남은 인원이 1명이하일때 채팅방 제거
		if (participants.size() <= 1) {
			chatroom.updateDeleted();
		}

		chatroomMongoRepository.save(chatroom);

	}

	@Override
	public List<ChatroomListResponseDto> findChatrooms(Long userId) {
		List<ChatroomDocument> chatrooms = chatroomMongoRepository.findChatroomsByUserId(userId);

		return chatrooms.stream().map(chatroom -> {
			Integer unreadCount = chatroom.getUnreadMessages().getOrDefault(userId.toString(), 0);
			Participant participant = chatroom.getParticipants().stream()
				.filter(user -> !user.getUserId().equals(userId))
				.findFirst()
				.orElse(null);
			String nickname = (participant != null) ? participant.getNickname() : "";
			String profileImgUrl = (participant != null) ? participant.getProfileImgUrl() : "";

			return ChatroomListResponseDto.builder()
				.chatroomId(chatroom.getId())
				.nickname(nickname)
				.ProfileImgUrl(profileImgUrl)
				.lastMessage(chatroom.getLastMessage())
				.unreadMessageCount(unreadCount)
				.build();
		}).collect(Collectors.toList());
	}

}
