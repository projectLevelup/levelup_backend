package com.sparta.levelup_backend.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.utill.JwtUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomOAuth2Handler implements AuthenticationSuccessHandler,
	AuthenticationFailureHandler {

	private final JwtUtils jwtUtils;
	private final FilterResponse filterResponse;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		Long id = customOAuth2User.getId();
		String email = customOAuth2User.getName();
		String nickName = customOAuth2User.getNickName();
		String provider = customOAuth2User.getProvider();

		if (provider.endsWith("new")) {
			request.setAttribute("id", id);
			request.setAttribute("email", email);
			request.setAttribute("nickName", nickName);
			request.setAttribute("provider", provider);

			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/v2/oauth2signup");
			requestDispatcher.forward(request, response);

		} else {
			Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
			Iterator<? extends GrantedAuthority> iterator = authorites.iterator();
			GrantedAuthority auth = iterator.next();
			String role = auth.getAuthority();

			String accessToken = jwtUtils.createAccessToken(email, id, nickName, role);
			String refreshToken = jwtUtils.createRefreshToken(email, id, nickName, role);

			response.addHeader("Authorization", accessToken);
			response.addHeader("Set-Cookie",
				"accessToken=" + accessToken + "; " + "Path=/; Domain=localhost; Max-Age=" + 30 * 60 + "; ");
			response.addHeader("Set-Cookie",
				"refreshToken=" + refreshToken + "; " + "Path=/; Domain=localhost; Max-Age=" + 12 * 60 * 60 + "; ");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/v2/home");
			requestDispatcher.forward(request, response);
		}
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		OAuth2AuthenticationException superException = (OAuth2AuthenticationException)exception;
		ErrorCode error = ErrorCode.valueOf(superException.getError().getErrorCode());

		filterResponse.responseErrorMsg(response, error.getStatus().value(), error.getCode(),
			error.getMessage());
	}
}
