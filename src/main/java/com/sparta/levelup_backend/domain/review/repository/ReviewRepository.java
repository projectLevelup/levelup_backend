package com.sparta.levelup_backend.domain.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

	default ReviewEntity findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
	}

	boolean existsByUserIdAndProductId(Long userId, Long productId);

	List<ReviewEntity> findByProduct(ProductEntity product);
}
