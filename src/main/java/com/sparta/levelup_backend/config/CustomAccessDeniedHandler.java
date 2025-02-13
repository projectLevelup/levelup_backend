package com.sparta.levelup_backend.config;

import com.sparta.levelup_backend.exception.common.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
        private final FilterResponse filterResponse;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        filterResponse.responseMsg(response, ErrorCode.UNAUTHORIZED_ACCESS.getStatus().value(),
            ErrorCode.UNAUTHORIZED_ACCESS.getMessage());
    }
}
