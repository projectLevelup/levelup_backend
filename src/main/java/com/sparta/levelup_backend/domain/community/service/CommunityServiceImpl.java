package com.sparta.levelup_backend.domain.community.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.community.repository.CommunityRepository;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CommunityServiceImpl implements CommunityService {
	private final UserRepository userRepository;
	private final CommunityRepository communityRepository;
	private final GameRepository gameRepository;

	@Override
	public CommunityResponseDto saveCommunity(Long userId, CommnunityCreateRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		GameEntity game = gameRepository.findByIdOrElseThrow(dto.getGameId());
		checkIsDeleted(game);

		CommunityEntity community = communityRepository.save(
			new CommunityEntity(dto.getTitle(), dto.getContent(), user, game));

		return CommunityResponseDto.of(community, user, game);
	}

	private void checkIsDeleted(GameEntity game) {
		if (game.getIsDeleted()) {
			throw new DuplicateException(GAME_ISDELETED);
		}
	}
}
