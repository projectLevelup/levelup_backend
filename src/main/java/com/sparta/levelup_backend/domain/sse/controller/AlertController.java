package com.sparta.levelup_backend.domain.sse.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.sse.service.AlertService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v3")
@RequiredArgsConstructor
public class AlertController {
	private final AlertService alertService;

	@GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> userChangeAlert(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		SseEmitter alert = alertService.alertSubscribe(userDetails.getId(), lastEventId);
		return new ResponseEntity(alert, HttpStatus.OK);
	}

	@PostMapping("/sse/allRead")
	public ApiResponse<Void> readAllAlert(@AuthenticationPrincipal CustomUserDetails userDetails) {
		alertService.readAllAlert(userDetails.getId());
		return success(HttpStatus.OK, ALERT_ALL_READ_SUCCESS);
	}

	@PostMapping("/sse/read/{alertId}")
	public ApiResponse<Void> readAlert(@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long alertId) {
		alertService.readAlert(userDetails.getId(), alertId);
		return success(HttpStatus.OK, ALERT_READ_SUCCESS);
	}
}
