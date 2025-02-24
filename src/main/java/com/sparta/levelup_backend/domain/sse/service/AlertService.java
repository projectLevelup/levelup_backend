package com.sparta.levelup_backend.domain.sse.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.levelup_backend.domain.sse.dto.response.AlertLogResponseDto;
import com.sparta.levelup_backend.domain.sse.entity.AlertMessageEntity;

public interface AlertService {

	SseEmitter alertSubscribe(Long userId, String lastEventId);

	void sendAlertMessage(Long userId, AlertMessageEntity sseMessage, Long logId);

	void sendAlertMessage(SseEmitter alert, String alertId, AlertMessageEntity alertMessageEntity);

	void readAllAlert(Long userId);

	void readAlert(Long userId, Long alertId);

	Page<AlertLogResponseDto> findLogByuserId(Long userId, Pageable pageable);

}
