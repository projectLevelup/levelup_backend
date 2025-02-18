package com.sparta.levelup_backend.domain.chat.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sparta.levelup_backend.domain.chat.dto.ChatroomListResponseDto;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomResponseDto;
import com.sparta.levelup_backend.domain.chat.entity.ChatroomEntity;
import com.sparta.levelup_backend.domain.chat.entity.ChatroomParticipantEntity;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomParticipantRepository;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomQueryRepository;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {

	private final UserRepository userRepository;
	private final ChatroomRepository chatroomRepository;
	private final ChatroomParticipantRepository cpRepository;
	private final ChatroomQueryRepository chatroomQueryRepository;

	@Transactional
	@Override
	public ChatroomResponseDto createChatroom(Long userId, Long targetUserId, String title) {

		if(chatroomQueryRepository.existsChatroomByUsers(userId, targetUserId)){
			throw new DuplicateException(DUPLICATE_CHATROOM);
		}

		UserEntity authUser = userRepository.findByIdOrElseThrow(userId);
		UserEntity targetUser = userRepository.findByIdOrElseThrow(targetUserId);

		String chatroomTitle = (StringUtils.hasText(title))
			? authUser.getNickName() + ", " + targetUser.getNickName()
			: title;

		ChatroomEntity chatroom = chatroomRepository.save(
			ChatroomEntity.builder()
				.title(chatroomTitle)
				.build()
		);

		ChatroomParticipantEntity authParticipant = ChatroomParticipantEntity.builder()
			.user(authUser)
			.chatroom(chatroom)
			.build();

		ChatroomParticipantEntity targetParticipant = ChatroomParticipantEntity.builder()
			.user(targetUser)
			.chatroom(chatroom)
			.build();

		chatroom.getUserSet().add(authParticipant);
		chatroom.getUserSet().add(targetParticipant);
		chatroom.updateParticipantsCount(chatroom.getUserSet().size());

		return ChatroomResponseDto.from(chatroom);
	}

	@Transactional
	@Override
	public void leaveChatroom(Long userId, Long chatroomId) {
		ChatroomEntity chatroom = chatroomRepository.findByIdOrElseThrow(chatroomId);

		// 채팅방 참가 여부 확인
		if (!cpRepository.existsByUserIdAndChatroomId(userId, chatroomId)) {
			throw new NotFoundException(PARTICIPANT_ISDELETED);
		}

		// 채팅방 참가 이력 삭제
		cpRepository.deleteByUserIdAndChatroomId(userId, chatroomId);

		// 채팅방에 남은 인원 1명 이하일때 채팅방 삭제
		if (cpRepository.countByChatroomId(chatroomId) <= 1) {
			chatroom.delete(); //
			cpRepository.deleteByChatroomId(chatroomId);
		}

		chatroomRepository.save(chatroom);

	}

	/**
	 * 채팅방 리스트 조회
	 */
	@Override
	public List<ChatroomListResponseDto> findChatrooms(Long userId) {
		return chatroomQueryRepository.findAllChatrooms(userId);
	}

}
