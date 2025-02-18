package com.sparta.levelup_backend.domain.auth.service;

import com.sparta.levelup_backend.domain.auth.dto.request.OAuthUserRequestDto;
import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;
import jakarta.validation.Valid;

public interface AuthService {

	void signUpUser(SignUpUserRequestDto dto);

	void oAuth2signUpUser(OAuthUserRequestDto dto);
}
