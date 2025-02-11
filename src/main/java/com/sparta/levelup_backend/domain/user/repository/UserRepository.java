package com.sparta.levelup_backend.domain.user.repository;

import java.util.Optional;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotBlank;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
	boolean existsByEmail(@NotBlank String email);
	Optional<UserEntity> findByEmail(String email);
}
