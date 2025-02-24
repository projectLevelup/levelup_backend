package com.sparta.levelup_backend.domain.sse.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertLogResponseDto {

	Long id;

	Long userId;

	String message;

	boolean isSend;

	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
