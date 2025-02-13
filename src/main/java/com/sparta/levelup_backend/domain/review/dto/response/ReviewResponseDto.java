package com.sparta.levelup_backend.domain.review.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private Long productId;
    private Long userId;
    private String contents;
    private Integer starScore;

    public ReviewResponseDto(ReviewEntity review) {
        this.reviewId = review.getId();
        this.productId = review.getProduct().getId();
        this.userId = review.getUser().getId();
        this.contents = review.getContents();
        this.starScore = review.getStarScore();
    }

    @QueryProjection
    public ReviewResponseDto(Long reviewId, Long userId, String contents, Integer starScore) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.contents = contents;
        this.starScore = starScore;
    }
}
