package com.sparta.levelup_backend.domain.email.event;

import com.sparta.levelup_backend.domain.email.dto.request.SendEmailDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailEvent {
	SendEmailDto sendEmailDto;
}
