package com.sparta.levelup_backend.domain.review.service;

import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;

public interface ReviewService {

    ReviewResponseDto reviewSave(ReviewRequestDto dto, Long userId, Long productId);

    void reviewDelete(Long userId, Long productId, Long reviewId);
}
