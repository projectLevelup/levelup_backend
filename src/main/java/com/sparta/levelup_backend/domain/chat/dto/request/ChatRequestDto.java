package com.sparta.levelup_backend.domain.chat.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ChatRequestDto {
	private final String message;
}
