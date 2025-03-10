package com.sparta.levelup_backend.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparta.levelup_backend.domain.chat.document.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ChatResponseDto {
	private final Long userId;
	private final String nickname;
	private final String message;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime timestamp = LocalDateTime.now();

	@JsonCreator
	public ChatResponseDto(
		@JsonProperty("userId") Long userId,
		@JsonProperty("nickname") String nickname,
		@JsonProperty("message") String message,
		@JsonProperty("timestamp") LocalDateTime timestamp) {
		this.userId = userId;
		this.nickname = nickname;
		this.message = message;
		this.timestamp = timestamp;
	}

	public static ChatResponseDto from(ChatMessage msg) {
		return new ChatResponseDto(
			msg.getUserId(),
			msg.getNickname(),
			msg.getMessage(),
			msg.getTimestamp()
		);
	}
}
