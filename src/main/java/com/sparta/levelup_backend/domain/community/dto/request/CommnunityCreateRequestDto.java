package com.sparta.levelup_backend.domain.community.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommnunityCreateRequestDto {
	private final String title;
	private final String content;
	private final Long gameId;
}
