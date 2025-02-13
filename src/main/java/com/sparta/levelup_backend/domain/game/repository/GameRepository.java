package com.sparta.levelup_backend.domain.game.repository;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
}
