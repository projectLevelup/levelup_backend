package com.sparta.levelup_backend.domain.game.dto.requestDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateGameRequestDto {
	private final String name;
	private final String imgUrl;
	private final String genre;
}
