package com.sparta.levelup_backend.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.sparta.levelup_backend.domain.comment.entity.CommentEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {
	private final Long commentId;
	private final String nickname;
	private final String content;
	private final LocalDateTime createdAt;

	public static CommentResponseDto from(CommentEntity comment) {
		return new CommentResponseDto(comment.getId(), comment.getUser().getNickName(), comment.getContent(),
			comment.getCreatedAt());
	}
}
