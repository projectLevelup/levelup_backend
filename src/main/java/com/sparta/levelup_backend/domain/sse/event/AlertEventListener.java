package com.sparta.levelup_backend.domain.sse.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sparta.levelup_backend.domain.sse.service.AlertService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AlertEventListener {
	private final AlertService alertService;

	@Async
	@EventListener
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void eventListener(AlertEvent alertEvent) {
		alertService.sendAlertMessage(alertEvent.getUserId(), alertEvent.getAlertMessageEntity());
	}
}
