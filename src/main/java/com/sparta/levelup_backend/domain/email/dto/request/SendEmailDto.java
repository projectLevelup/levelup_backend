package com.sparta.levelup_backend.domain.email.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendEmailDto {

	private String to;

	private String subject;

	private String message;
}
