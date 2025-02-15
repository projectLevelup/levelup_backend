package com.sparta.levelup_backend.domain.chat.dto;

import com.sparta.levelup_backend.domain.chat.entity.ChatroomEntity;
import com.sparta.levelup_backend.domain.chat.entity.ChatroomParticipantEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatroomListResponseDto {

	private final Long chatroomId;
	private final String nickname;

	public static ChatroomListResponseDto from(ChatroomEntity chatroom, Long userId) {

		String nickname = chatroom.getUserSet().stream()
			.map(ChatroomParticipantEntity::getUser)
			.filter(user -> !user.getId().equals(userId))
			.map(UserEntity::getNickName)
			.findFirst()
			.orElse(chatroom.getTitle());

		return new ChatroomListResponseDto(chatroom.getId(), nickname);
	}
}
