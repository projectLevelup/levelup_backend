package com.sparta.levelup_backend.domain.game.dto.requestDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateGameRequestDto {
	private final String name;
	private final String ImgUrl;
	private final String genre;
}
