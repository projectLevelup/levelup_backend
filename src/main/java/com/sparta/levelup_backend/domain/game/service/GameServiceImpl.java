package com.sparta.levelup_backend.domain.game.service;

import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repsitory.GameRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.utill.UserRole;

import lombok.RequiredArgsConstructor;

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
			//TODO: globalExceptionHandler 추가시 변경할 것.
			throw new RuntimeException(String.valueOf(ErrorCode.FORBIDDEN_ACCESS));
		}
		return gameRepository.save(
			GameEntity.builder()
				.name(name)
				.imgUrl(imgUrl)
				.genre(genre)
				.user(user)
				.build());
	}
}
