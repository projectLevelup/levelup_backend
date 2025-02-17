package com.sparta.levelup_backend.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ChatMessageDto {

	private Long chatroomId;
	private String nickname;
	private String message;
}
