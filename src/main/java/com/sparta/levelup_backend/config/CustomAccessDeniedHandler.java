package com.sparta.levelup_backend.config;

import static com.sparta.levelup_backend.exception.common.ErrorCode.FORBIDDEN_ACCESS;

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

        filterResponse.responseErrorMsg(response,
            FORBIDDEN_ACCESS.getStatus().value(),
            FORBIDDEN_ACCESS.getCode(),
            FORBIDDEN_ACCESS.getMessage());
    }
}
