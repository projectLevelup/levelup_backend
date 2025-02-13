package com.sparta.levelup_backend.domain.game.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.game.dto.requestDto.UpdateGameRequestDto;
import com.sparta.levelup_backend.domain.game.dto.responseDto.GameListResponseDto;
import com.sparta.levelup_backend.domain.game.dto.responseDto.GameResponseDto;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
	private final GameRepository gameRepository;
	private final UserRepository userRepository;

	@Override
	public GameEntity saveGame(String name, String imgUrl, String genre, Long userId) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		checkAdminAuth(user);

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
	public GameEntity findGame(Long userId, Long gameId) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		checkAdminAuth(user);

		GameEntity game = gameRepository.findByIdOrElseThrow(gameId);
		checkIsDeleted(game);

		return game;
	}

	@Override
	public GameEntity updateGame(Long userId, Long gameId, UpdateGameRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		checkAdminAuth(user);

		GameEntity game = gameRepository.findByIdOrElseThrow(gameId);
		checkIsDeleted(game);

		if (Objects.nonNull(dto.getName()))
			game.updateName(dto.getName());
		if (Objects.nonNull(dto.getImgUrl()))
			game.updateImgUrl(dto.getImgUrl());
		if (Objects.nonNull(dto.getGenre()))
			game.updateGenre(dto.getGenre());

		return game;
	}

	@Override
	public GameListResponseDto findGames() {
		return new GameListResponseDto(gameRepository.findAll()
			.stream()
			.filter(game -> !game.getIsDeleted())
			.map(game -> new GameResponseDto(game.getName(), game.getImgUrl(),
				game.getGenre()))
			.toList());
	}

	@Override
	public void deleteGame(Long userId, Long gameId) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		checkAdminAuth(user);

		GameEntity game = gameRepository.findByIdOrElseThrow(gameId);
		checkIsDeleted(game);

		game.deleteGame();
	}

	private void checkAdminAuth(UserEntity user) {
		if (!user.getRole().equals(UserRole.ADMIN)) {
			throw new ForbiddenException(FORBIDDEN_ACCESS);
		}
	}

	private void checkIsDeleted(GameEntity game) {
		if (game.getIsDeleted()) {
			throw new DuplicateException(GAME_ISDELETED);
		}
	}
}
