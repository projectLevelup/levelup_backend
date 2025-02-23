package com.sparta.levelup_backend.domain.sse.event.publisher;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sparta.levelup_backend.domain.sse.service.SseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SseEventListener {
	private final SseService sseService;

	@Async
	@EventListener
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void userEventListener(SseEvent sseEvent) {
		sseService.sendSseMessage(sseEvent.getUser().getId(), sseEvent.getUserSseMessageEntity());

	}
}
