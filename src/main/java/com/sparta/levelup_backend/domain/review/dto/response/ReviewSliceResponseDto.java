package com.sparta.levelup_backend.domain.review.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ReviewSliceResponseDto {

    private final Long reviewId;
    private final Long userId;
    private final String contents;
    private final Integer starScore;

    @QueryProjection
    public ReviewSliceResponseDto(Long reviewId, Long userId, String contents, Integer starScore) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.contents = contents;
        this.starScore = starScore;
    }
}
