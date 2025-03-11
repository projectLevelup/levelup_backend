package com.sparta.levelup_backend.domain.game.repository;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.exception.game.GameException;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
	default GameEntity findByIdOrElseThrow(Long id){
		return findById(id).orElseThrow(() -> new GameException(GAME_NOT_FOUND));
	}

}
