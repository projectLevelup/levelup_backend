package com.sparta.levelup_backend.domain.review.dto.response;

import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewResponseDto {

    private final Long reviewId;
    private final Long productId;
    private final Long userId;
    private final String contents;
    private final Integer starScore;

    public ReviewResponseDto(ReviewEntity review) {
        this.reviewId = review.getId();
        this.productId = review.getProduct().getId();
        this.userId = review.getUser().getId();
        this.contents = review.getContents();
        this.starScore = review.getStarScore();
    }
}
