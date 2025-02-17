package com.sparta.levelup_backend.domain.community.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityListResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityReadResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.community.repository.CommunityRepository;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.exception.common.PageOutOfBoundsException;

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

	@Transactional(readOnly = true)
	@Override
	public CommunityListResponseDto findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityEntity> communityPage = communityRepository.findAllByIsDeletedFalse(pageable);

		CommunityListResponseDto responseDto = new CommunityListResponseDto(
			communityPage.stream()
				.map(community -> CommunityReadResponseDto.of(community, community.getUser(), community.getGame()))
				.toList()
		);

		if (communityPage.getTotalPages() <= page) {
			throw new PageOutOfBoundsException(PAGE_OUT_OF_BOUNDS);
		}

		if (responseDto.getCommunityList().isEmpty()) {
			throw new NotFoundException(COMMUNITY_NOT_FOUND);
		}

		return responseDto;
	}

	private void checkIsDeleted(GameEntity game) {
		if (game.getIsDeleted()) {
			throw new DuplicateException(GAME_ISDELETED);
		}
	}
}
