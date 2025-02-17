package com.sparta.levelup_backend.domain.user.service;

import com.sparta.levelup_backend.domain.user.dto.request.ChangePasswordDto;
import com.sparta.levelup_backend.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserImgUrlReqeustDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;
import jakarta.validation.Valid;

public interface UserService {

    UserResponseDto findUserById(String role, Long id);

    UserResponseDto findUser(Long id);

    UserResponseDto updateUser(Long id, UpdateUserRequestDto dto);

    void changePassword(Long id, ChangePasswordDto dto);

    UserResponseDto updateImgUrl(Long id, UpdateUserImgUrlReqeustDto dto);

    void deleteUser(Long id, @Valid DeleteUserRequestDto dto);
}
