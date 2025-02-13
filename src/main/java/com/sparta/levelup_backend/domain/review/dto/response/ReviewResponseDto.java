package com.sparta.levelup_backend.domain.review.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {

	private Long reviewId;
	private Long userId;
	private String nickname;
	private String contents;
	private Integer starScore;

	public ReviewResponseDto(ReviewEntity review) {
		this.reviewId = review.getId();
		this.userId = review.getUser().getId();
		this.nickname = review.getUser().getNickName();
		this.contents = review.getContents();
		this.starScore = review.getStarScore();
	}

	@QueryProjection
	public ReviewResponseDto(Long reviewId, Long userId, String nickname, String contents, Integer starScore) {
		this.reviewId = reviewId;
		this.userId = userId;
		this.nickname = nickname;
		this.contents = contents;
		this.starScore = starScore;
	}
}
