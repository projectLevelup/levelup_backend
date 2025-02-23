package com.sparta.levelup_backend.domain.sse.event.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.sparta.levelup_backend.domain.sse.entity.SseMessageEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SseEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	public void publisher(UserEntity user, SseMessageEntity sseMessageEntity) {
		applicationEventPublisher.publishEvent(new SseEvent(user, sseMessageEntity));
	}
}
