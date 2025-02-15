package com.sparta.levelup_backend.proto;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final TempJwtUtils testJwtUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		String token = testJwtUtils.generateToken(authentication);
		response.addHeader("Authorization", "Bearer " + token);

		Cookie jwtCookie = new Cookie("JWT", token);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setPath("/");
		response.addCookie(jwtCookie);

		response.sendRedirect("/");
	}
}
