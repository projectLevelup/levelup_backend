package com.sparta.levelup_backend.config;

import java.io.IOException;

import io.jsonwebtoken.Claims;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sparta.levelup_backend.domain.auth.service.CustomUserDetailsService;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.utill.JwtUtils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
	private final CustomUserDetailsService customUserDetailsService;
	private final RequestMatcher WHITE_LIST = new AntPathRequestMatcher("/v1/sign**");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
			if(WHITE_LIST.matches(request)) {
				filterChain.doFilter(request, response);
				return;
			}

			String jwt = request.getHeader("Authorization");


			if(jwt.isBlank()){
				throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
			}

			try{
				Claims claims = jwtUtils.extractClaims(jwtUtils.substringToken(jwt));
				if(claims == null){
					throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
				}

				String email = claims.getSubject();
				
				CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService
																.loadUserByUsername(email);

				Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authToken);
				filterChain.doFilter(request, response);
			} catch (SecurityException | MalformedJwtException e) {
				sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
			} catch (ExpiredJwtException e) {
				sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
			} catch (UnsupportedJwtException e) {
				sendError(response, HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
			} catch (Exception e) {
				sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "그 외의 오류입니다..");
			}
	}

	private void sendError(HttpServletResponse response,int errorCode, String msg) throws IOException {
		response.setStatus(errorCode);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(msg);
		response.getWriter().flush();
	}
}
