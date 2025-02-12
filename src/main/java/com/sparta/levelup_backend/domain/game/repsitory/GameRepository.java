package com.sparta.levelup_backend.domain.game.repsitory;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.exception.common.ErrorCode;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
	default GameEntity findByIdOrElseThrow(Long id){
		// TODO: exception 바꾸기
		return findById(id).orElseThrow(() -> new RuntimeException(String.valueOf(ErrorCode.USER_NOT_FOUND)));
	}
}
