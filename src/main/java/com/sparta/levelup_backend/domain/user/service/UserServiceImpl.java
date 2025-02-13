package com.sparta.levelup_backend.domain.user.service;

import com.sparta.levelup_backend.domain.user.dto.response.UserResponseDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.ForbiddenException;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserEntity findById(Long userId) {
       return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserResponseDto findUserById(String role, Long id) {

        if(role.equals("ROLE_ADMIN")){
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
}
