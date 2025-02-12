package com.sparta.levelup_backend.domain.game.dto.responseDto;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GameResponseDto {
	private final String name;
	private final String imgUrl;
	private final String genre;

	public static GameResponseDto from(GameEntity game){
		return new GameResponseDto(game.getName(), game.getImgUrl(), game.getGenre());
	}
}
