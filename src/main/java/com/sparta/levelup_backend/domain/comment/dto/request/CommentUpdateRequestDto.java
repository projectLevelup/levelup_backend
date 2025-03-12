package com.sparta.levelup_backend.domain.comment.dto.request;

import static com.sparta.levelup_backend.domain.comment.dto.CommentValidMessage.*;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentUpdateRequestDto {
	private final Long commentId;
	@NotBlank(message = CONTENT_REQUIRED)
	private final String content;
}
