package com.sparta.levelup_backend.config;

import com.sparta.levelup_backend.exception.common.NotFoundException;
import java.io.IOException;

import io.jsonwebtoken.Claims;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sparta.levelup_backend.domain.auth.service.CustomUserDetailsService;
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
    private final FilterResponse filterResponse;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException, NotFoundException {
        if (WHITE_LIST.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader("Authorization");

        try {
            if (jwt == null || jwt.isBlank()) {
                throw new NotFoundException(ErrorCode.TOKEN_NOT_FOUND);
            }
            Claims claims = jwtUtils.extractClaims(jwtUtils.substringToken(jwt));
            if (claims == null) {
                throw new NotFoundException(ErrorCode.TOKEN_NOT_FOUND);
            }

            String email = claims.getSubject();

            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService
                .loadUserByUsername(email);

            Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            filterResponse.responseMsg(response, ErrorCode.INVALID_JWT_TOKEN.getStatus().value(),
                ErrorCode.INVALID_JWT_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            filterResponse.responseMsg(response, ErrorCode.EXPIRED_JWT_TOKEN.getStatus().value(),
                ErrorCode.EXPIRED_JWT_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            filterResponse.responseMsg(response, ErrorCode.INVALID_FORMAT_TOKEN.getStatus().value(),
                ErrorCode.INVALID_FORMAT_TOKEN.getMessage());
        } catch (NotFoundException e) {
            filterResponse.responseMsg(response, ErrorCode.TOKEN_NOT_FOUND.getStatus().value(),
                ErrorCode.TOKEN_NOT_FOUND.getMessage());
        } catch (Exception e) {
			filterResponse.responseMsg(response, ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value(),
				ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    private void sendError(HttpServletResponse response, int errorCode, String msg)
        throws IOException {
        response.setStatus(errorCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(msg);
        response.getWriter().flush();
    }
}
