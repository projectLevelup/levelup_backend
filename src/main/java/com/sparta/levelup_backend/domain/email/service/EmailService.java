package com.sparta.levelup_backend.domain.email.service;

import com.sparta.levelup_backend.domain.email.dto.request.SendEmailDto;

public interface EmailService {

	void mailSender(SendEmailDto sendEmailDto);

}