package com.sparta.levelup_backend.domain.review.dto.response;

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
}
