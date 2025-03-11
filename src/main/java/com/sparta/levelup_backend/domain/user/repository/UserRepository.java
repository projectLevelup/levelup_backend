package com.sparta.levelup_backend.domain.user.repository;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.exception.user.UserException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	boolean existsByEmail(String email);

	default void existsByEmailOrElseThrow(String email) {
		if (existsByEmail(email)) {
			throw new UserException(DUPLICATE_EMAIL);
		}
	}

	Optional<UserEntity> findByEmail(String email);

	default UserEntity findByEmailOrElseThrow(String email) {

		return findByEmail(email).orElseThrow(() -> new UserException(USER_NOT_FOUND));
	}

	default UserEntity findByIdOrElseThrow(Long userId) {

		return findById(userId)
			.orElseThrow(() -> new UserException(USER_NOT_FOUND));
	}
}
