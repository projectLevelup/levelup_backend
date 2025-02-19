package com.sparta.levelup_backend.domain.review.dto.request;

import static com.sparta.levelup_backend.domain.review.dto.ReviewValidMessage.*;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewRequestDto {

	@Size(max = 100, message = INVALID_REVIEW_CONTENTS)
	private final String contents;

	@NotNull(message = STAR_SCORE_REQUIRED)
	@Range(min = 1, max = 5, message = STAR_SCORE_RANGE)
	private final Integer starScore;

}
