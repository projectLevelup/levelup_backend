package com.sparta.levelup_backend.domain.user.repository;

import static com.sparta.levelup_backend.exception.common.ErrorCode.USER_NOT_FOUND;

import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.exception.common.EmailDuplicatedException;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
	boolean existsByEmail(@NotBlank String email);

	default void existsByEmailOrElseThrow(String email){
		if(!existsByEmail(email)){
			throw new EmailDuplicatedException();
		}
	}

	Optional<UserEntity> findByEmail(String email);

	default UserEntity findByEmailOrElseThrow(String email){
		return findByEmail(email).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
	};

	default UserEntity findByIdOrElseThrow(Long userId) {
		return findById(userId)
			.orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
	}
}
