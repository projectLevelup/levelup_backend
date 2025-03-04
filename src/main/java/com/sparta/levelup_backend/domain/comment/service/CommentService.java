package com.sparta.levelup_backend.domain.comment.service;

import com.sparta.levelup_backend.domain.comment.dto.request.CommentCreateRequestDto;
import com.sparta.levelup_backend.domain.comment.dto.request.CommentUpdateRequestDto;
import com.sparta.levelup_backend.domain.comment.dto.response.CommentResponseDto;

public interface CommentService {
	CommentResponseDto saveComment(Long userId, CommentCreateRequestDto dto);

	CommentResponseDto updateComment(Long userId, CommentUpdateRequestDto dto);
}
