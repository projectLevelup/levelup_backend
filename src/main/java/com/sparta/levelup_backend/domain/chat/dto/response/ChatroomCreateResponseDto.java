package com.sparta.levelup_backend.domain.chat.dto.response;

import java.util.List;

import com.sparta.levelup_backend.domain.chat.document.ChatroomDocument;
import com.sparta.levelup_backend.domain.chat.document.Participant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatroomCreateResponseDto {

	private final String chatroomId;
	private final String title;
	private final List<Participant> participants;

	public static ChatroomCreateResponseDto from(ChatroomDocument chatroom) {
		return new  ChatroomCreateResponseDto(
			chatroom.getId(),
			chatroom.getTitle(),
			chatroom.getParticipants()
		);
	}
}
