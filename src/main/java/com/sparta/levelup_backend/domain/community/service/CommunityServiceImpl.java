package com.sparta.levelup_backend.domain.community.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;
import static com.sparta.levelup_backend.utill.UserRole.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
import com.sparta.levelup_backend.domain.community.repository.CommunityQueryRepository;
import com.sparta.levelup_backend.domain.community.repository.CommunityRepository;
import com.sparta.levelup_backend.domain.community.repositoryES.CommunityESRepository;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.exception.common.NotFoundException;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CommunityServiceImpl implements CommunityService {
	private final UserRepository userRepository;
	private final CommunityRepository communityRepository;
	private final GameRepository gameRepository;
	private final CommentRepository commentRepository;

	private final CommunityESRepository communityESRepository;

	private final ElasticsearchClient elasticsearchClient;
	private final CommunityQueryRepository communityQueryRepository;

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
	public CommunityListResponseDto findAllByGameName(String gameName, Pageable pageable) {
		Slice<CommunityReadResponseDto> communityPage = communityQueryRepository.findAllByGameName(gameName, pageable);

		if (communityPage.isEmpty()) {
			throw new NotFoundException(COMMUNITY_NOT_FOUND);
		}
		return new CommunityListResponseDto(
			communityPage.stream().toList(), communityPage.hasNext()
		);
	}

	/**
	 * community 목록 검색
	 * 게임(카테고리라고 생각하변 편함)에 속한 글을 검색어를 통해 검색
	 * @param gameName 검색할 게임
	 * @param searchKeyword 제목 검색어
	 * @param pageable 0부터 시작
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public CommunityListResponseDto findCommunities(String gameName, String searchKeyword, Pageable pageable) {
		Slice<CommunityReadResponseDto> communityReadResponseDtoPages = communityQueryRepository.findCommunities(
			gameName, searchKeyword, pageable);

		if (communityReadResponseDtoPages.isEmpty()) {
			throw new NotFoundException(COMMUNITY_NOT_FOUND);
		}
		return new CommunityListResponseDto(
			communityReadResponseDtoPages.stream().toList(), communityReadResponseDtoPages.hasNext());
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

	/**
	 * community 목록 검색(elasticSearch 사용)
	 * 게임(카테고리라고 생각하변 편함)에 속한 글을 검색어를 통해 검색
	 * @param searchKeyword 제목 검색어 (null이면 모든 글 검색)
	 * @param gameName 검색할 게임 (null이면 게임 필터 없이 검색)
	 * @param page 기본값: 0
	 * @param size 기본값: 10
	 * @return
	 */
	@Override
	public CommunityListResponseDto findCommunitiesES(String searchKeyword, String gameName, int page,
		int size) {
		SearchRequest request = SearchRequest.of(s -> s
			.index("community")
			.from(page * size)
			.size(size + 1)
			.query(q -> q.bool(b -> {
				if (gameName != null && !gameName.isEmpty()) {
					b.filter(f -> f.term(t -> t.field("gameName").value(gameName)));
				}
				if (searchKeyword != null && !searchKeyword.isEmpty()) {
					b.must(m -> m.match(mq -> mq.field("title").query(searchKeyword)));
				}
				if ((gameName == null || gameName.isEmpty()) && (searchKeyword == null || searchKeyword.isEmpty())) {
					b.must(m -> m.matchAll(ma -> ma));
				}
				return b;
			})));

		SearchResponse<CommunityDocument> response;
		try {
			response = elasticsearchClient.search(request, CommunityDocument.class);
		} catch (IOException e) {
			throw new RuntimeException("Elasticsearch 검색 실패", e);
		}

		// TODO: 응답값에 아무 데이터가 없을때 예외가 제대로 발생되는지 -> 아무 값도 반환하지 않음.
		if (Objects.isNull(response.hits().total())) {
			throw new NotFoundException(COMMUNITY_NOT_FOUND);
		}

		boolean hasNext = false;
		if (size == response.hits().total().value()) {
			hasNext = true;
			response.hits().hits().remove(size - 1);
		}

		CommunityListResponseDto responseDto = new CommunityListResponseDto(
			response.hits().hits().stream().map(community -> {
				assert community.source() != null;
				return CommunityReadResponseDto.from(community.source());
			}).toList(), hasNext);

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
