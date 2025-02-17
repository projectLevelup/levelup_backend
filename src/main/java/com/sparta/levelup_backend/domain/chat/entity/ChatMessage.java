package com.sparta.levelup_backend.domain.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Getter
@Document(collection = "chat_messages")
public class ChatMessage {

	@Id
	private String id;
	private Long chatroomId;
	private String nickname;
	private String message;
	private LocalDateTime timestamp;

	public ChatMessage(Long chatroomId, String nickname, String message, LocalDateTime timestamp) {
		this.chatroomId = chatroomId;
		this.nickname = nickname;
		this.message = message;
		this.timestamp = timestamp;
	}
}
