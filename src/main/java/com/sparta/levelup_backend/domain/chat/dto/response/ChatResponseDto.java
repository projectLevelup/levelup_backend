package com.sparta.levelup_backend.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparta.levelup_backend.domain.chat.document.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {
	private Long userId;
	private String nickname;
	private String message;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime timestamp = LocalDateTime.now();

	public static ChatResponseDto from(ChatMessage msg) {
		return new ChatResponseDto(
			msg.getUserId(),
			msg.getNickname(),
			msg.getMessage(),
			msg.getTimestamp()
		);
	}
}
