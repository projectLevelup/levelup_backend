package com.sparta.levelup_backend.domain.user.repository;

import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import java.util.Optional;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(@NotBlank String email);

    Optional<UserEntity> findByEmail(String email);

    default UserEntity findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(
            () -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    ;

    default UserEntity findByIdOrElseThrow(Long userId) {
        return findById(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
