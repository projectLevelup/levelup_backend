package com.sparta.levelup_backend.proto;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sparta.levelup_backend.domain.auth.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TempJwtFilter extends OncePerRequestFilter {

	private final TempJwtUtils testJwtUtils;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain)
		throws ServletException, IOException {
		String token = getJwtFromRequest(request);

		if (StringUtils.hasText(token) && testJwtUtils.validateToken(token)) {
			String email = testJwtUtils.getUserEmailFromJWT(token);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			// SecurityContext에 Authentication 등록
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}


	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("JWT".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}