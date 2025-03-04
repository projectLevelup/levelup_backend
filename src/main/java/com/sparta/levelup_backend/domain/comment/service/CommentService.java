package com.sparta.levelup_backend.domain.comment.service;

import com.sparta.levelup_backend.domain.comment.dto.request.CommentRequestDto;
import com.sparta.levelup_backend.domain.comment.dto.response.CommentResponseDto;

public interface CommentService {
	CommentResponseDto saveComment(Long userId, CommentRequestDto dto);
}
