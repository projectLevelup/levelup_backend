package com.sparta.levelup_backend.domain.chat.repository;

import static com.sparta.levelup_backend.domain.chat.entity.QChatroomEntity.*;
import static com.sparta.levelup_backend.domain.chat.entity.QChatroomParticipantEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.levelup_backend.domain.chat.dto.ChatroomListResponseDto;
import com.sparta.levelup_backend.domain.chat.entity.QChatroomEntity;
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

	public List<ChatroomListResponseDto> findAllChatrooms(Long userId) {
		QChatroomParticipantEntity cpAuth = chatroomParticipantEntity;

		// 상대 참여 정보
		QChatroomParticipantEntity cpTarget = new QChatroomParticipantEntity("cpTarget");
		QChatroomEntity chatroom = chatroomEntity;

		return queryFactory
			.select(Projections.constructor(ChatroomListResponseDto.class,
											cpAuth.chatroom.id,
											cpTarget.user.nickName,
											cpTarget.user.imgUrl
			))
			.from(cpAuth)
			.join(cpAuth.chatroom, chatroom)
			.join(chatroom.userSet, cpTarget)
			.where(cpAuth.user.id.eq(userId)
					   .and(cpTarget.user.id.ne(userId)))
			.distinct()
			.fetch();
	}

}
