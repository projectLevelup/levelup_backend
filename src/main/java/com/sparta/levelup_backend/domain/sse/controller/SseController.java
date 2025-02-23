package com.sparta.levelup_backend.domain.sse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.sse.service.SseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class SseController {
	private final SseService sseService;

	@GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> userChangeAlert(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		SseEmitter alert = sseService.sseSubscribe(userDetails.getId(), lastEventId);
		return new ResponseEntity(alert, HttpStatus.OK);
	}

}
