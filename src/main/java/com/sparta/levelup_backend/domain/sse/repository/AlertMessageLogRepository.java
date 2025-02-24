package com.sparta.levelup_backend.domain.sse.repository;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.sse.dto.response.AlertLogResponseDto;
import com.sparta.levelup_backend.domain.sse.entity.AlertMessageLogEntity;
import com.sparta.levelup_backend.exception.common.NotFoundException;

public interface AlertMessageLogRepository extends JpaRepository<AlertMessageLogEntity, Long> {

	default AlertMessageLogEntity findByIdOrElseThrow(Long logId) {

		return findById(logId)
			.orElseThrow(() -> new NotFoundException(ALERT_LOG_MESSAGE_NOT_FOUND));
	}

	Page<AlertLogResponseDto> findAllByUserId(Long userId, Pageable pageable);
}
