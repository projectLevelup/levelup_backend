package com.sparta.levelup_backend.domain.sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.levelup_backend.domain.sse.entity.SseMessageEntity;

public interface SseService {

	SseEmitter sseSubscribe(Long id, String lastEventId);

	void sendSseMessage(Long userId, SseMessageEntity sseMessage);

	void sendSseMessage(SseEmitter alert, String alertId, SseMessageEntity sseMessageEntity);

}
