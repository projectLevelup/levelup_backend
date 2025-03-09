package com.sparta.levelup_backend.domain.community.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.levelup_backend.domain.community.dto.response.CommunityReadResponseDto;
import com.sparta.levelup_backend.domain.community.dto.response.QCommunityReadResponseDto;
import com.sparta.levelup_backend.domain.community.entity.QCommunityEntity;
import com.sparta.levelup_backend.domain.game.entity.QGameEntity;
import com.sparta.levelup_backend.domain.user.entity.QUserEntity;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommunityQueryRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public Page<CommunityReadResponseDto> findCommunities(String gameName, String searchKeyword, Pageable pageable){
		QCommunityEntity community = new QCommunityEntity("community");
		QUserEntity user = new QUserEntity("user");
		QGameEntity game = new QGameEntity("game");

		List<CommunityReadResponseDto> communityEntities = queryFactory
			.select(new QCommunityReadResponseDto(
				Expressions.stringTemplate("CAST({0} AS STRING)", community.id),
				community.title,
				user.nickName,
				game.name))
			.from(community)
			.leftJoin(community.user, user)
			.leftJoin(community.game, game)
			.where(community.game.name.eq(gameName))
			.where(community.title.contains(searchKeyword))
			.where(community.isDeleted.eq(false))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(communityEntities);
	}
}
