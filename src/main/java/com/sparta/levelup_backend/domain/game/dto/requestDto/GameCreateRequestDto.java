package com.sparta.levelup_backend.domain.game.dto.requestDto;

import static com.sparta.levelup_backend.domain.game.dto.GameValidMessage.*;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GameCreateRequestDto {
	@NotBlank(message = GAME_NAME_REQUIRED)
	private final String name;
	@NotBlank(message = GAME_GENRE_REQUIRED)
	private final String genre;
}
