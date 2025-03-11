package com.sparta.levelup_backend.domain.review.repository;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import com.sparta.levelup_backend.exception.review.ReviewException;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

	default ReviewEntity findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new ReviewException(REVIEW_NOT_FOUND));
	}

	boolean existsByUserIdAndProductId(Long userId, Long productId);

}
