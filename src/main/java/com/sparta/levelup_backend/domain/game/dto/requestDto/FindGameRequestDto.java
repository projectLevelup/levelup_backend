package com.sparta.levelup_backend.domain.game.dto.requestDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FindGameRequestDto {
	private final Long gameId;
}
