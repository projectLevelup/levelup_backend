package com.sparta.levelup_backend.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessageDto {
	private String nickname;
	private String message;
}
