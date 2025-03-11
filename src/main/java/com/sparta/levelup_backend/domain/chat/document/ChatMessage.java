package com.sparta.levelup_backend.domain.chat.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@Document(collection = "message")
public class ChatMessage {

	@Id
	private String id;
	private String chatroomId;
	private Long userId;
	private String nickname;
	private String message;

	@Builder.Default
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime timestamp = LocalDateTime.now();

	@JsonCreator
	public ChatMessage(
		@JsonProperty("id") String id,
		@JsonProperty("chatroomId") String chatroomId,
		@JsonProperty("userId") Long userId,
		@JsonProperty("nickname") String nickname,
		@JsonProperty("message") String message, // 추가된 부분
		@JsonProperty("timestamp") LocalDateTime timestamp) {
		this.id = id;
		this.chatroomId = chatroomId;
		this.userId = userId;
		this.nickname = nickname;
		this.message = message;
		this.timestamp = timestamp;
	}

	public static ChatMessage of(String chatroomId, Long userId, String nickname, String message) {
		return ChatMessage.builder()
			.chatroomId(chatroomId)
			.userId(userId)
			.nickname(nickname)
			.message(message)
			.build();
	}

}
