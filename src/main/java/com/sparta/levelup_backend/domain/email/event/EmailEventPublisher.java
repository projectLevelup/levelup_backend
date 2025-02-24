package com.sparta.levelup_backend.domain.email.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.sparta.levelup_backend.domain.email.dto.request.SendEmailDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EmailEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	public void publisher(SendEmailDto sendEmailDto) {
		applicationEventPublisher.publishEvent(new EmailEvent(sendEmailDto));
	}
}
