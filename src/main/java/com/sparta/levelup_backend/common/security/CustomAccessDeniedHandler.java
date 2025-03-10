package com.sparta.levelup_backend.common.security;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final FilterResponse filterResponse;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {

		filterResponse.responseErrorMsg(response,
			FORBIDDEN_ACCESS.getStatus().value(),
			FORBIDDEN_ACCESS.getCode(),
			FORBIDDEN_ACCESS.getMessage());
	}
}
