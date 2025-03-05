package com.sparta.levelup_backend.domain.community.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;
import static com.sparta.levelup_backend.utill.UserRole.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.comment.dto.response.CommentResponseDto;
import com.sparta.levelup_backend.domain.comment.entity.CommentEntity;
import com.sparta.levelup_backend.domain.comment.repository.CommentRepository;
import com.sparta.levelup_backend.domain.community.document.CommunityDocument;
import com.sparta.levelup_backend.domain.community.dto.request.CommnunityCreateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.request.CommunityUpdateRequestDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityCommentResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityListResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityReadResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityResponseDto;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.community.repository.CommunityRepository;
import com.sparta.levelup_backend.domain.community.repositoryES.CommunityESRepository;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
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

	private final CommunityESRepository communityESRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	private final String COMMUNITY_CACHE_KEY = "community:";
	private final String COMMUNITY_ZSET_KEY = "community_view";
	private final CommentRepository commentRepository;

	@Override
	public CommunityResponseDto saveCommunity(Long userId, CommnunityCreateRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		GameEntity game = gameRepository.findByIdOrElseThrow(dto.getGameId());
		checkGameIsDeleted(game);

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

	@Override
	public CommunityCommentResponseDto findById(Long communityId) {
		CommunityEntity community = communityRepository.findByIdOrElseThrow(communityId);
		checkCommunityIsDeleted(community);

		List<CommentEntity> comments = commentRepository.findByCommunityIdAndIsDeletedFalse(communityId);

		return CommunityCommentResponseDto.of(community, comments.stream().map(CommentResponseDto::from).toList());
	}

	@Override
	public CommunityResponseDto update(Long userId, CommunityUpdateRequestDto dto) {
		CommunityEntity community = communityRepository.findByIdOrElseThrow(dto.getCommunityId());
		checkAuth(community, userId);
		checkCommunityIsDeleted(community);

		if (Objects.nonNull(dto.getTitle())) {
			community.updateTitle(dto.getTitle());
		}
		if (Objects.nonNull(dto.getContent())) {
			community.updateContent(dto.getContent());
		}

		return CommunityResponseDto.from(community);
	}

	@Override
	public void delete(Long userId, Long communityId) {
		CommunityEntity community = communityRepository.findByIdOrElseThrow(communityId);
		checkAuth(community, userId);
		checkCommunityIsDeleted(community);

		community.deleteCommunity();
	}

	// community 생성(elasticSearch 사용)
	@Override
	public CommunityResponseDto saveCommunityES(Long userId, CommnunityCreateRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		GameEntity game = gameRepository.findByIdOrElseThrow(dto.getGameId());
		CommunityEntity community = communityRepository.save(
			new CommunityEntity(dto.getTitle(), dto.getContent(), user, game));
		CommunityDocument communityDocument = communityESRepository.save(CommunityDocument.from(community));

		return CommunityResponseDto.from(communityDocument);
	}

	// community 목록 검색(elasticSearch 사용)
	@Override
	public CommunityListResponseDto findCommunitiesES(String searchKeyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityDocument> communityDocuments = communityESRepository.findByTitleAndIsDeletedFalse(searchKeyword,
			pageable);

		CommunityListResponseDto responseDto = new CommunityListResponseDto(communityDocuments.stream()
			.map(CommunityReadResponseDto::from)
			.toList());

		if (communityDocuments.getTotalPages() <= page) {
			throw new PageOutOfBoundsException(PAGE_OUT_OF_BOUNDS);
		}

		if (responseDto.getCommunityList().isEmpty()) {
			throw new NotFoundException(COMMUNITY_NOT_FOUND);
		}

		return responseDto;
	}

	// community 단건 조회(elasticSearch 사용)
	@Override
	public CommunityResponseDto findCommunityES(String communityId) {
		CommunityDocument communityDocument = communityESRepository.findByIdOrElseThrow(communityId);
		return CommunityResponseDto.from(communityDocument);
	}

	// community 수정(elasticSearch 사용)
	@Override
	public CommunityResponseDto updateCommunityES(Long userId, CommunityUpdateRequestDto dto) {
		CommunityEntity community = communityRepository.findByIdOrElseThrow(dto.getCommunityId());
		CommunityDocument communityDocument = communityESRepository.findByIdOrElseThrow(
			String.valueOf(dto.getCommunityId()));
		checkAuth(community, userId);
		checkCommunityIsDeleted(community);

		if (communityDocument.getIsDeleted()) {
			throw new DuplicateException(COMMUNITY_ISDELETED);
		}

		if (Objects.nonNull(dto.getTitle())) {
			community.updateTitle(dto.getTitle());
			communityDocument.updateTitle(dto.getTitle());
		}
		if (Objects.nonNull(dto.getContent())) {
			community.updateContent(dto.getContent());
			communityDocument.updateContent(dto.getContent());
		}

		communityESRepository.save(communityDocument);

		return CommunityResponseDto.from(communityDocument);
	}

	// community 삭제(elasticSearch 사용)
	@Override
	public void deleteCommunityES(Long userId, Long communityId) {
		CommunityEntity community = communityRepository.findByIdOrElseThrow(communityId);
		CommunityDocument communityDocument = communityESRepository.findByIdOrElseThrow(String.valueOf(communityId));
		checkAuth(community, userId);
		checkCommunityIsDeleted(community);

		community.deleteCommunity();
		communityDocument.updateIsDeleted(true);
		communityESRepository.save(communityDocument);
	}

	/**
	 * community 생성(redis활용)
	 * @param userId 사용자 Id
	 * @param dto title, content, gameId
	 * @return CommunityResponseDto
	 */
	@Override
	public CommunityResponseDto saveCommunityRedis(Long userId, CommnunityCreateRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		GameEntity game = gameRepository.findByIdOrElseThrow(dto.getGameId());
		checkGameIsDeleted(game);
		CommunityEntity community = communityRepository.save(
			new CommunityEntity(dto.getTitle(), dto.getContent(), user, game));

		String redisKey = COMMUNITY_CACHE_KEY + community.getId();

		Map<String, Object> communityMap = Map.of(
			"communityId", community.getId(),
			"title", community.getTitle(),
			"userEmail", user.getEmail(),
			"gameName", game.getName()
		);
		redisTemplate.opsForHash().putAll(redisKey, communityMap);
		redisTemplate.opsForZSet().add(COMMUNITY_ZSET_KEY, redisKey, 0);
		return CommunityResponseDto.of(community, user, game);
	}

	/**
	 * community 검색(redis 활용)
	 * @param searchKeyword 검색할 단어
	 * @param page 페이지 수
	 * @param size 한 페이지에 표시할 데이터 수
	 * @return CommunityListResponseDto
	 */
	@Override
	public CommunityListResponseDto findCommunityRedis(String searchKeyword, int page, int size) {
		Set<String> keys = redisTemplate.keys(COMMUNITY_CACHE_KEY + "*");
		List<String> matchedCommunities = new ArrayList<>();

		if (keys == null) {
			throw new NotFoundException(COMMUNITY_NOT_FOUND);
		}

		for (String key : keys) {
			Map<String, Object> community = redisTemplate.<String, Object>opsForHash().entries(key);
			String title = community.get("title").toString();

			if (title.toLowerCase().contains(searchKeyword.toLowerCase())) {
				matchedCommunities.add(key);
			}
		}

		if (matchedCommunities.isEmpty()) {
			throw new NotFoundException(COMMUNITY_NOT_FOUND);
		}

		if (page * size >= matchedCommunities.size()) {
			throw new PageOutOfBoundsException(PAGE_OUT_OF_BOUNDS);
		}

		matchedCommunities.sort((a, b) -> {
			Double scoreA = redisTemplate.opsForZSet().score(COMMUNITY_ZSET_KEY, a);
			Double scoreB = redisTemplate.opsForZSet().score(COMMUNITY_ZSET_KEY, b);

			scoreA = (scoreA != null) ? scoreA : 0.0;
			scoreB = (scoreB != null) ? scoreB : 0.0;

			return Double.compare(scoreB, scoreA);
		});

		List<CommunityReadResponseDto> results = new ArrayList<>();
		for (String key : matchedCommunities.stream().skip((long)page * size).limit(size).toList()) {
			Map<String, Object> result = redisTemplate.<String, Object>opsForHash().entries(key);
			results.add(new CommunityReadResponseDto(
				String.valueOf(result.get("communityId")),
				(String)result.get("title"),
				(String)result.get("userEmail"),
				(String)result.get("gameName")));
			incrementViews(key);
		}

		return new CommunityListResponseDto(results);
	}

	/**
	 * community 수정(redis 활용)
	 * @param userId 사용자 Id
	 * @param dto communityId, title, content
	 * @return CommunityResponseDto
	 */
	@Override
	public CommunityResponseDto updateCommunityRedis(Long userId, CommunityUpdateRequestDto dto) {
		CommunityEntity community = communityRepository.findByIdOrElseThrow(dto.getCommunityId());

		String key = COMMUNITY_CACHE_KEY + community.getId();
		Map<String, Object> communityMap = redisTemplate.<String, Object>opsForHash().entries(key);
		checkAuth(community, userId);
		checkCommunityIsDeleted(community);

		if (Objects.nonNull(dto.getTitle())) {
			community.updateTitle(dto.getTitle());
			communityMap.put("title", dto.getTitle());
		}
		if (Objects.nonNull(dto.getContent())) {
			community.updateContent(dto.getContent());
		}

		redisTemplate.opsForHash().putAll(key, communityMap);
		communityRepository.save(community);

		return CommunityResponseDto.from(community);
	}

	/**
	 * community 삭제(redis 활용)
	 * @param userId 사용자 Id
	 * @param communityId community Id
	 */
	@Override
	public void deleteCommunityRedis(Long userId, Long communityId) {
		String key = COMMUNITY_CACHE_KEY + communityId;
		CommunityEntity community = communityRepository.findByIdOrElseThrow(communityId);
		checkAuth(community, userId);
		checkCommunityIsDeleted(community);

		redisTemplate.delete(key);
		community.deleteCommunity();

	}

	public void incrementViews(String communityKey) {
		redisTemplate.opsForZSet().incrementScore(COMMUNITY_ZSET_KEY, communityKey, 1);
	}

	private void checkGameIsDeleted(GameEntity game) {
		if (game.getIsDeleted()) {
			throw new DuplicateException(GAME_ISDELETED);
		}
	}

	private void checkCommunityIsDeleted(CommunityEntity community) {
		if (community.getIsDeleted()) {
			throw new DuplicateException(COMMUNITY_ISDELETED);
		}
	}

	private void checkAuth(CommunityEntity community, Long userId) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		if (!community.getUser().getId().equals(userId) && !user.getRole().equals(ADMIN)) {
			throw new ForbiddenException(FORBIDDEN_ACCESS);
		}
	}
}
