package com.sparta.levelup_backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;

import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
	boolean existsByEmail(@NotBlank String email);

}
