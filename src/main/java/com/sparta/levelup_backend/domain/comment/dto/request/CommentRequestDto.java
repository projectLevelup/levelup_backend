package com.sparta.levelup_backend.domain.comment.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequestDto {
	private final Long communityId;
	private final String content;
}
