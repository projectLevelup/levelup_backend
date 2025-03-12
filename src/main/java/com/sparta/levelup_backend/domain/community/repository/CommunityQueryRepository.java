package com.sparta.levelup_backend.domain.community.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityReadResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.QCommunityReadResponseDto;
import com.sparta.levelup_backend.domain.community.entity.QCommunityEntity;
import com.sparta.levelup_backend.domain.game.entity.QGameEntity;
import com.sparta.levelup_backend.domain.user.entity.QUserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommunityQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Slice<CommunityReadResponseDto> findAllByGameName(String gameName, Pageable pageable) {
		QCommunityEntity community = new QCommunityEntity("community");
		QUserEntity user = new QUserEntity("user");
		QGameEntity game = new QGameEntity("game");

		List<CommunityReadResponseDto> communities = queryFactory
			.select(new QCommunityReadResponseDto(
				Expressions.stringTemplate("CAST({0} AS STRING)", community.id),
				community.title,
				user.nickName,
				game.name))
			.from(community)
			.leftJoin(community.user, user)
			.leftJoin(community.game, game)
			.where(gameName != null && !gameName.isEmpty() ? community.game.name.eq(gameName) : null)
			.where(community.isDeleted.eq(false))
			.orderBy(community.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()+1) //다음 페이지가 있는지 확인하기 위해 데이터를 1개 더 불러옴
			.fetch();

		// 다음 페이지 여부 확인
		boolean hasNext = communities.size() > pageable.getPageSize();

		//마지막 페이지가 아니면 더 가져온 페이지는 필요 없기 때문에 삭제
		if (hasNext) {
			communities.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(communities, pageable, hasNext);
	}
}
