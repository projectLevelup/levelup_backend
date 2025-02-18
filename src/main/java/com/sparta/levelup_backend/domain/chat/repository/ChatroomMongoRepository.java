package com.sparta.levelup_backend.domain.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sparta.levelup_backend.domain.chat.document.ChatroomDocument;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;

public interface ChatroomMongoRepository extends MongoRepository<ChatroomDocument, String> {

	@Query(value="{ 'participants.userId' : { $all: ?0 } }")
	long countByParticipantsUserIds(List<Long> userIds);

	default ChatroomDocument findByIdOrThrow(String id) {
		return findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.CHATROOM_NOT_FOUND));
	}

	@Query("{ 'participants.userId' : ?0 }")
	List<ChatroomDocument> findChatroomsByUserId(Long userId);

}
