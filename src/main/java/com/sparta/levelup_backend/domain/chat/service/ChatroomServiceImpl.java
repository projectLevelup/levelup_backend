package com.sparta.levelup_backend.domain.chat.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.chat.dto.ChatroomResponseDto;
import com.sparta.levelup_backend.domain.chat.entity.ChatroomEntity;
import com.sparta.levelup_backend.domain.chat.entity.ChatroomParticipantEntity;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomParticipantRepository;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomQueryRepository;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;

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

		ChatroomEntity chatroom = chatroomRepository.save(
			ChatroomEntity.builder()
				.title(title)
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
	public Boolean leaveChatroom(Long userId, Long chatroomId) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);

		// 기존에 참여된 방인지 확인
		if(!cpRepository.existsByUserIdAndChatroomId(userId, chatroomId)) {
			return false;
		}

		// 참여 기록 제거
		cpRepository.deleteByUserIdAndChatroomId(userId, chatroomId);

		return true;
	}

	/**
	 * 채팅방 리스트 조회
	 */
	@Override
	public List<ChatroomResponseDto> findChatrooms(Long userId) {
		List<ChatroomParticipantEntity> chatroomInfoList = cpRepository.findAllByUserId(userId);

		List<ChatroomEntity> chatroomList = chatroomInfoList.stream()
			.map(ChatroomParticipantEntity::getChatroom)
			.toList();

		return chatroomList.stream()
			.map(ChatroomResponseDto::from)
			.toList();
	}

}
