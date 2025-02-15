package com.sparta.levelup_backend.domain.chat.repository;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.chat.entity.ChatroomEntity;
import com.sparta.levelup_backend.exception.common.NotFoundException;

public interface ChatroomRepository extends JpaRepository<ChatroomEntity, Long> {

	default ChatroomEntity findByIdOrElseThrow(Long chatroomId) {
		return findById(chatroomId).orElseThrow(() -> new NotFoundException(CHATROOM_NOT_FOUND));
	}
}
