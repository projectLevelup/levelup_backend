package com.sparta.levelup_backend.domain.email.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.domain.email.dto.request.SendEmailDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender javaMailSender;

	@Override
	public void mailSender(SendEmailDto sendEmailDto) {

		try {
			MimeMessage mail = javaMailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mail, false, "UTF-8");
			mailHelper.setTo(sendEmailDto.getTo());
			mailHelper.setSubject(sendEmailDto.getSubject());
			mailHelper.setText(sendEmailDto.getMessage());
			javaMailSender.send(mail);
		} catch (MessagingException e) {

			throw new RuntimeException(e);
		}

	}
}
