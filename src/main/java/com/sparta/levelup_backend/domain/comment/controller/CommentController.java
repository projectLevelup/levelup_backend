package com.sparta.levelup_backend.domain.comment.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.comment.dto.request.CommentRequestDto;
import com.sparta.levelup_backend.domain.comment.dto.response.CommentResponseDto;
import com.sparta.levelup_backend.domain.comment.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v2/comment")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ApiResponse<CommentResponseDto> saveComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@Valid @RequestBody CommentRequestDto dto) {
		Long userId = customUserDetails.getId();

		CommentResponseDto responseDto = commentService.saveComment(userId, dto);
		return success(OK, COMMENT_SAVE_SUCCESS, responseDto);
	}
}
