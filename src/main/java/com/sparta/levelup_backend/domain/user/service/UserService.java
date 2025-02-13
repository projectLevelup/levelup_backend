package com.sparta.levelup_backend.domain.user.service;

import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto findUserById(String role, Long id);


    UserResponseDto findUser(Long id);
}
