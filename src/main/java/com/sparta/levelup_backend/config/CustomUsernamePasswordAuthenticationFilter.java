package com.sparta.levelup_backend.config;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.levelup_backend.domain.auth.dto.request.SignInUserRequestDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * json과 requestParam방식을 모두 지원하도록 작성하였습니다.
	 * requestParam으로 받을 필요 있나?
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		String email;
		String password;

		if(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
			try {
				SignInUserRequestDto signInUserRequestDto =	objectMapper.readValue(request.getReader().lines().collect(Collectors.joining()), SignInUserRequestDto.class);

				email = signInUserRequestDto.getEmail();
				password = signInUserRequestDto.getPassword();

			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "임시 오류: json 변환실패.");
			}
		}else {

			email = request.getParameter("email");
			password = obtainPassword(request);
		}
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password);

		return authenticationManager.authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) {
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws
		IOException,
		ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
