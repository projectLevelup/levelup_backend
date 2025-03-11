package com.sparta.levelup_backend.domain.chat.service;

import static com.sparta.levelup_backend.enums.ErrorCode.*;
import static java.util.Arrays.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.util.StringUtils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.chat.document.ChatroomDocument;
import com.sparta.levelup_backend.domain.chat.document.Participant;
import com.sparta.levelup_backend.domain.chat.dto.response.ChatroomCreateResponseDto;
import com.sparta.levelup_backend.domain.chat.dto.response.ChatroomListResponseDto;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomMongoRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.chat.ChatException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {

	private final MongoTemplate mongoTemplate;
	private final ChatroomMongoRepository chatroomMongoRepository;
	private final UserRepository userRepository;

	private static final String UNREAD_MESSAGES = "unreadMessages.";
	private static final String LAST_MESSAGE = "lastMessage";
	private static final String DB_ID = "_id";

	@Override
	@Transactional
	public ChatroomCreateResponseDto createChatroom(Long userId, Long targetUserId, String title) {

		if (userId.equals(targetUserId)) {
			throw new ChatException(INVALID_CHATROOM_CREATE);
		}

		// 상대와의 채팅방이 이미 존재하는 지 확인
		if (chatroomMongoRepository.countByParticipantsUserIds(asList(targetUserId, userId)) > 0) {
			throw new ChatException(DUPLICATE_CHATROOM);
		}

		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		UserEntity targetUser = userRepository.findByIdOrElseThrow(targetUserId);

		ChatroomDocument chatroom = buildChatroom(title, user, targetUser);
		ChatroomDocument savedChatroom = chatroomMongoRepository.save(chatroom);

		return ChatroomCreateResponseDto.from(savedChatroom);
	}

	@Override
	@Transactional
	public void leaveChatroom(Long userId, String chatroomId) {
		ChatroomDocument chatroom = chatroomMongoRepository.findByIdOrThrow(chatroomId);

		// 채팅방에 참여자로 존재하는 지 확인
		boolean isParticipant = chatroom.getParticipants().stream()
			.anyMatch(user -> user.getUserId().equals(userId));

		if(!isParticipant) {
			throw new ChatException(PARTICIPANT_ISDELETED);
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
	public Slice<ChatroomListResponseDto> findChatrooms(Long userId, Pageable pageable) {
		Slice<ChatroomDocument> chatrooms = chatroomMongoRepository.findChatroomsByUserId(userId, pageable);

		List<ChatroomListResponseDto> content = chatrooms.getContent().stream()
			.map(chatroom -> ChatroomListResponseDto.from(chatroom, userId))
			.collect(Collectors.toList());

		return new SliceImpl<>(content, pageable, chatrooms.hasNext());
	}

	@Override
	public void updateUnreadCountAndLastMessage(String chatroomId, Long publisherId, String Message) {
		ChatroomDocument chatroom = chatroomMongoRepository.findByIdOrThrow(chatroomId);

		// 마지막 메시지 업데이트
		Update update = new Update();
		update.set(LAST_MESSAGE, Message);

		// 발행자가 아닌 사용자의 unreadMessageCount 증가
		for (Participant participant : chatroom.getParticipants()) {
			if (!participant.getUserId().equals(publisherId)) {
				update.inc(UNREAD_MESSAGES + participant.getUserId(), 1);
			}
		}

		Query query = new Query(where(DB_ID).is(chatroomId));
		mongoTemplate.updateFirst(query, update, ChatroomDocument.class);
	}

	@Override
	public void updateUnreadCountZero(String chatroomId, Long readUserId) {
		ChatroomDocument chatroom = chatroomMongoRepository.findByIdOrThrow(chatroomId);

		// 읽은 사람의 unreadMessageCount 0으로 초기화
		Update update = new Update();
		for (Participant participant : chatroom.getParticipants()) {
			if (participant.getUserId().equals(readUserId)) {
				update.set(UNREAD_MESSAGES + participant.getUserId(), 0);
			}
		}

		Query query = new Query(where(DB_ID).is(chatroomId));
		mongoTemplate.updateFirst(query, update, ChatroomDocument.class);
	}

	/**
	 * 채팅방 Document 빌드 메서드
	 */
	private ChatroomDocument buildChatroom(String title, UserEntity user, UserEntity targetUser) {

		// 제목을 적지 않았을 경우 참여자 닉네임으로 자동 생성
		String chatroomTitle = hasText(title)
			? title
			: user.getNickName() + ", " + targetUser.getNickName();

		// 안 읽은 메시지값 기본값 0 으로 설정
		Map<String, Integer> unreadMessages = new HashMap<>();
		unreadMessages.put(user.getId().toString(), 0);
		unreadMessages.put(targetUser.getId().toString(), 0);

		List<Participant> participants = asList(new Participant(user), new Participant(targetUser));

		return ChatroomDocument.builder()
			.title(title)
			.participants(participants)
			.lastMessage("")
			.unreadMessages(unreadMessages)
			.build();
	}

}
