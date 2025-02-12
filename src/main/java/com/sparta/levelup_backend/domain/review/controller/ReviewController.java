package com.sparta.levelup_backend.domain.review.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.REVIEW_SUCCESS;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;
import com.sparta.levelup_backend.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/products/{productId}/reviews")
    public ApiResponse<ReviewResponseDto> reviewSave(@Valid  @RequestBody ReviewRequestDto dto, @PathVariable Long productId) {

        Long userId = 1L; // 임시 사용자 ID 값 - 추후 JWT 토큰값에서 ID값 가져오는 것으로 변경
        ReviewResponseDto result = reviewService.reviewSave(dto, userId, productId);
        return ApiResponse.success(REVIEW_SUCCESS, result);
    }
}
