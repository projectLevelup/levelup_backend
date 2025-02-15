package com.sparta.levelup_backend.domain.chat.service;

import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.domain.chat.dto.ChatroomResponseDto;
import com.sparta.levelup_backend.domain.chat.entity.ChatroomEntity;
import com.sparta.levelup_backend.domain.chat.entity.ChatroomParticipantEntity;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomParticipantRepository;
import com.sparta.levelup_backend.domain.chat.repository.ChatroomRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {

	private final UserRepository userRepository;
	private final ChatroomRepository chatroomRepository;
	private final ChatroomParticipantRepository cpRepository;

	@Override
	public ChatroomResponseDto createChatroom(Long userId, String title) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);

		ChatroomEntity chatroom = ChatroomEntity.builder()
			.title(title)
			.build();

		chatroom = chatroomRepository.save(chatroom);

		ChatroomParticipantEntity chatroomParticipant = ChatroomParticipantEntity.builder()
			.user(user)
			.chatroom(chatroom)
			.build();

		cpRepository.save(chatroomParticipant);

		return ChatroomResponseDto.from(chatroom);
	}

	@Override
	public ChatroomResponseDto createPrivateChatroom(Long userId, Long targetUserId, String title) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		UserEntity targetUser = userRepository.findByIdOrElseThrow(targetUserId);

		ChatroomEntity chatroom = ChatroomEntity.builder()
			.title(title)
			.build();

		chatroom = chatroomRepository.save(chatroom);

		ChatroomParticipantEntity cp1 = ChatroomParticipantEntity.builder()
			.user(user)
			.chatroom(chatroom)
			.build();

		ChatroomParticipantEntity cp2 = ChatroomParticipantEntity.builder()
			.user(targetUser)
			.chatroom(chatroom)
			.build();

		cpRepository.save(cp1);
		cpRepository.save(cp2);

		return ChatroomResponseDto.from(chatroom);
	}

}
