package com.sparta.levelup_backend.domain.game.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repsitory.GameRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{
	private final GameRepository gameRepository;
	private final UserRepository userRepository;

	@Override
	public GameEntity saveGame(String name, String imgUrl, String genre, Long userId) {
		//TODO: orElseThrow()처리 다시 할 것
		UserEntity user = userRepository.findById(userId).orElseThrow();

		if(!user.getRole().equals(UserRole.ADMIN)){
			throw new ForbiddenException(FORBIDDEN_ACCESS);
		}

		return gameRepository.save(
			GameEntity.builder()
				.name(name)
				.imgUrl(imgUrl)
				.genre(genre)
				.user(user)
				.build());
	}

	@Transactional(readOnly = true)
	@Override
	public GameEntity findGame(Long gameId) {
		GameEntity game = gameRepository.findByIdOrElseThrow(gameId);
		if(game.getIsDeleted()){
			throw new NotFoundException(GAME_NOT_FOUND);
		}
		return game;
	}

	@Override
	public void deleteGame(Long gameId) {
		GameEntity game = gameRepository.findByIdOrElseThrow(gameId);
		game.deleteGame();
	}
}
