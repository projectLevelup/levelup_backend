package com.sparta.levelup_backend.domain.game.service;

import com.sparta.levelup_backend.domain.game.dto.requestDto.UpdateGameRequestDto;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;

public interface GameService {
	GameEntity saveGame(String name, String imgUrl, String genre, Long userId);

	GameEntity findGame(Long gameId);

	void deleteGame(Long gameId);

	GameEntity updateGame(Long gameId, UpdateGameRequestDto dto);
}
