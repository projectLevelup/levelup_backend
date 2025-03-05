package com.sparta.levelup_backend.domain.comment.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentUpdateRequestDto {
	private final Long commentId;
	private final String content;
}
