package com.sparta.levelup_backend.domain.sse.repository;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.sse.entity.SseMessageEntity;
import com.sparta.levelup_backend.exception.common.NotFoundException;

public interface SseMessageRepository extends JpaRepository<SseMessageEntity, Long> {

	default SseMessageEntity findByIdOrElseThrow(Long messageId) {

		return findById(messageId)
			.orElseThrow(() -> new NotFoundException(ALERT_MESSAGE_NOT_FOUND));
	}

	List<SseMessageEntity> findAllByUserId(Long userId);
}
