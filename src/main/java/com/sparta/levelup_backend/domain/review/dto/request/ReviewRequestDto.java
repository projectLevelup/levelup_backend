package com.sparta.levelup_backend.domain.review.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewRequestDto {

    private final String contents;
    private final Integer starScore;

}
