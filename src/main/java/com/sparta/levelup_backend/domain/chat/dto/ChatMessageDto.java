package com.sparta.levelup_backend.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

	private Long chatroomId;
	private String nickname;
	private String message;
}
