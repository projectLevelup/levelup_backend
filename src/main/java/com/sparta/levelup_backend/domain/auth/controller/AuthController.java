package com.sparta.levelup_backend.domain.auth.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.SIGNUP_SUCCESS;
import static com.sparta.levelup_backend.common.ApiResponse.success;
import static org.springframework.http.HttpStatus.CREATED;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.auth.dto.request.OAuthUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import com.sparta.levelup_backend.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v2")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ApiResponse<Void> signUpUser(@Valid @RequestBody SignUpUserRequestDto dto) {
        authService.signUpUser(dto);
        return success(CREATED, SIGNUP_SUCCESS);
    }

    @PostMapping("/oauth2signup")
    public ApiResponse<Void> oAuth2signUpUser(@Valid @RequestBody OAuthUserRequestDto dto) {
        authService.oAuth2signUpUser(dto);
        return success(CREATED, SIGNUP_SUCCESS);
    }
}
