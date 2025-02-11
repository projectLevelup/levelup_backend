package com.sparta.levelup_backend.domain.auth.service;

import com.sparta.levelup_backend.domain.auth.dto.request.SignUpUserRequestDto;

public interface AuthService {

	void signUpUser(SignUpUserRequestDto dto);

}
