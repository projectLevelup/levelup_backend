package com.sparta.levelup_backend.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;

import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmail(@NotBlank String email);

	Optional<UserEntity> findByEmail(String email);

	default UserEntity findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
	}
}
