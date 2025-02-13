package com.sparta.levelup_backend.domain.user.service;

import com.sparta.levelup_backend.domain.user.dto.request.ChangePasswordDto;
import com.sparta.levelup_backend.domain.user.dto.request.UpdateUserRequestDto;
import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.CurrentPasswordNotMatched;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.exception.common.PasswordConfirmNotMatched;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserResponseDto findUserById(String role, Long id) {

        if (role.equals("ROLE_ADMIN")) {
            UserEntity user = userRepository.findByIdOrElseThrow(id);
            return UserResponseDto.of(user);
        }
        throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
    }

    @Override
    public UserResponseDto findUser(Long id) {
        UserEntity user = userRepository.findByIdOrElseThrow(id);
        return UserResponseDto.of(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UpdateUserRequestDto dto) {

        UserEntity user = userRepository.findByIdOrElseThrow(id);

        if (dto.getEmail() != null) {
            userRepository.existsByEmailOrElseThrow(dto.getEmail());
            user.updateEmail(dto.getEmail());
        }

        if (dto.getNickName() != null) {
            user.updateNickName(dto.getNickName());
        }

        if (dto.getImgUrl() != null) {
            user.updateImgUrl(dto.getImgUrl());
        }

        if (dto.getPhoneNumber() != null) {
            user.updatePhoneNumber(dto.getPhoneNumber());
        }

        return UserResponseDto.of(user);
    }

    @Override
    public void changePassword(Long id, ChangePasswordDto dto) {
        UserEntity user = userRepository.findByIdOrElseThrow(id);

        if (bCryptPasswordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            if (dto.getNewPassword().equals(dto.getPasswordConfirm())) {
                user.changePassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
            } else {
                throw new PasswordConfirmNotMatched();
            }
        } else {
            throw new CurrentPasswordNotMatched();
        }
    }
}
