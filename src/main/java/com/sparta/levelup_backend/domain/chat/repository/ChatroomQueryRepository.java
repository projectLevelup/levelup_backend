package com.sparta.levelup_backend.domain.chat.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.levelup_backend.domain.chat.entity.QChatroomParticipantEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatroomQueryRepository {

	private final JPAQueryFactory queryFactory;

	public boolean existsChatroomByUsers(Long userId, Long targetUserId) {
		QChatroomParticipantEntity participant = new QChatroomParticipantEntity("participant");

		return queryFactory
			.selectOne()
			.from(participant)
			.where(participant.chatroom.id.in(
				queryFactory
					.select(participant.chatroom.id)
					.from(participant)
					.where(participant.user.id.eq(userId))
			))
			.where(participant.user.id.eq(targetUserId))
			.fetchFirst() != null;
	}


}
