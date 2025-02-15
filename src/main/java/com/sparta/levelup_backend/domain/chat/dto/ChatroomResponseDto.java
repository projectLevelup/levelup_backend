package com.sparta.levelup_backend.domain.chat.dto;

import com.sparta.levelup_backend.domain.chat.entity.ChatroomEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatroomResponseDto {

	private final Long chatroomId;
	private final String title;
	private final Integer participantsCount;

	public static ChatroomResponseDto from(ChatroomEntity chatroom) {
		return new ChatroomResponseDto(chatroom.getId(), chatroom.getTitle(), chatroom.getParticipantsCount());
	}
}
