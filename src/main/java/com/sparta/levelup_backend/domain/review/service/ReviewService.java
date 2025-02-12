package com.sparta.levelup_backend.domain.review.service;

import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;

public interface ReviewService {

    ReviewResponseDto SaveReview(ReviewRequestDto dto, Long userId, Long productId);

    void DeleteReview(Long userId, Long productId, Long reviewId);
}
