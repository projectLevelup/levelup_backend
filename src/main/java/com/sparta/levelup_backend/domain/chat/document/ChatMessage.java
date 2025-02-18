package com.sparta.levelup_backend.domain.chat.document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {

	@Id
	private String id;
	private Long chatroomId;
	private String nickname;
	private String message;
	private LocalDateTime timestamp;

	@Builder.Default
	private List<String> readUsers = new ArrayList<>();
}
