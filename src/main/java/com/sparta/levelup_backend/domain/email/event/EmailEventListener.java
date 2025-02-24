package com.sparta.levelup_backend.domain.email.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sparta.levelup_backend.domain.email.service.EmailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EmailEventListener {
	private final EmailService emailService;

	@Async
	@EventListener
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void eventListener(EmailEvent emailEvent) {
		emailService.mailSender(emailEvent.getSendEmailDto());
	}
}
