package com.sparta.levelup_backend.domain.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;

public interface ReviewService {

	ReviewResponseDto saveReview(ReviewRequestDto dto, Long userId, Long productId);

	void deleteReview(Long userId, Long productId, Long reviewId);

	Slice<ReviewResponseDto> findReviews(Long productId, Pageable pageable);

}
