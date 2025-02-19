package com.sparta.levelup_backend.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.utill.JwtUtils;
import com.sparta.levelup_backend.utill.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;
	private final List<RequestMatcher> WHITE_LIST = Arrays.asList(
		new AntPathRequestMatcher("/"),
		new AntPathRequestMatcher("/v2/sign**"),
		new AntPathRequestMatcher("/oauth2/authorization/naver"),
		new AntPathRequestMatcher("/v2/oauth2sign*"));
	private final OrRequestMatcher orRequestMatcher = new OrRequestMatcher(WHITE_LIST);
	private final FilterResponse filterResponse;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException, NotFoundException {

		if (orRequestMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = request.getHeader("Authorization");
		String refreshToken = extractToken(request, "refreshToken");
		accessToken = extractToken(request, "accessToken");

		try {
			if (refreshToken != null) {
				if (isTokenExpired(accessToken)) {
					String token = jwtUtils.refresingToken(refreshToken);
					response.addHeader("Authorization", token);
					response.addHeader("Set-Cookie", "accessToken=" + token);
				} else if (isTokenExpired(refreshToken)) {
					String token = jwtUtils.refresingToken(accessToken);
					response.addHeader("Set-Cookie", "refreshToken=" + token);
				}
			}

			Claims accessTokenClaims = jwtUtils.extractClaims(jwtUtils.substringToken(accessToken));

			String email = accessTokenClaims.getSubject();
			String role = accessTokenClaims.get("role", String.class);
			role = role.substring(5);
			Long id = Long.parseLong(accessTokenClaims.get("id", String.class));
			String nickName = accessTokenClaims.get("nickName", String.class);

			UserEntity tokenUser = UserEntity.builder()
				.id(id)
				.email(email)
				.nickName(nickName)
				.role(UserRole.valueOf(role))
				.build();

			CustomUserDetails userDetails = new CustomUserDetails(tokenUser);

			Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authToken);
			filterChain.doFilter(request, response);
		} catch (SecurityException | MalformedJwtException e) {
			filterResponse.responseErrorMsg(response,
				ErrorCode.INVALID_JWT_TOKEN.getStatus().value(),
				ErrorCode.INVALID_JWT_TOKEN.getCode(),
				ErrorCode.INVALID_JWT_TOKEN.getMessage());
		} catch (ExpiredJwtException e) {
			filterResponse.responseErrorMsg(response,
				ErrorCode.EXPIRED_JWT_TOKEN.getStatus().value(),
				ErrorCode.EXPIRED_JWT_TOKEN.getCode(),
				ErrorCode.EXPIRED_JWT_TOKEN.getMessage());
		} catch (UnsupportedJwtException e) {
			filterResponse.responseErrorMsg(response,
				ErrorCode.INVALID_FORMAT_TOKEN.getStatus().value(),
				ErrorCode.INVALID_FORMAT_TOKEN.getCode(),
				ErrorCode.INVALID_FORMAT_TOKEN.getMessage());
		} catch (NotFoundException e) {
			filterResponse.responseErrorMsg(response, ErrorCode.TOKEN_NOT_FOUND.getStatus().value(),
				ErrorCode.TOKEN_NOT_FOUND.getCode(),
				ErrorCode.TOKEN_NOT_FOUND.getMessage());
		} catch (Exception e) {
			filterResponse.responseErrorMsg(response,
				ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value(),
				ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
				ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
		}
	}

	public String extractToken(HttpServletRequest request, String tokenName) {
		String token = null;
		if (request.getHeader("Cookie") != null) {
			String cookie = request.getHeader("Cookie");
			String[] cookies = cookie.split("; ");
			for (String cookiedata : cookies) {
				if (cookiedata.startsWith(tokenName)) {
					int index = cookiedata.indexOf("=");
					token = cookiedata.substring(index + 1);
				}
			}
		}
		return token;
	}

	public boolean isTokenExpired(String token) throws Exception {

		try {

			if (token == null || token.isBlank()) {
				throw new NotFoundException(ErrorCode.TOKEN_NOT_FOUND);
			}
			Claims claims = jwtUtils.extractClaims(jwtUtils.substringToken(token));
			if (claims == null) {
				throw new NotFoundException(ErrorCode.TOKEN_NOT_FOUND);
			}
			return false;

		} catch (ExpiredJwtException e) {
			return true;
		}
	}
}
