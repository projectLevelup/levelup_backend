package com.sparta.levelup_backend.domain.comment.service;

import static com.sparta.levelup_backend.enums.UserRole.*;
import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.comment.dto.request.CommentCreateRequestDto;
import com.sparta.levelup_backend.domain.comment.dto.request.CommentUpdateRequestDto;
import com.sparta.levelup_backend.domain.comment.dto.response.CommentResponseDto;
import com.sparta.levelup_backend.domain.comment.entity.CommentEntity;
import com.sparta.levelup_backend.domain.comment.repository.CommentRepository;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.community.repository.CommunityRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.user.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
	private final UserRepository userRepository;
	private final CommunityRepository communityRepository;
	private final CommentRepository commentRepository;

	@Override
	public CommentResponseDto saveComment(Long userId, CommentCreateRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		CommunityEntity community = communityRepository.findByIdOrElseThrow(dto.getCommunityId());

		CommentEntity comment = commentRepository.save(new CommentEntity(dto.getContent(), user, community));
		return CommentResponseDto.from(comment);
	}

	@Override
	public CommentResponseDto updateComment(Long userId, CommentUpdateRequestDto dto) {
		CommentEntity comment = commentRepository.findByIdOrElseThrow(dto.getCommentId());

		checkAuth(comment, userId);
		checkCommentIsDeleted(comment);

		comment.updateContent(dto.getContent());
		return CommentResponseDto.from(comment);
	}

	@Override
	public void deleteComment(Long userId, Long commentId) {
		CommentEntity comment = commentRepository.findByIdOrElseThrow(commentId);
		checkAuth(comment, userId);
		checkCommentIsDeleted(comment);

		comment.deleteComment();
	}

	private void checkAuth(CommentEntity comment, Long userId) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		if (!comment.getUser().getId().equals(userId) && !user.getRole().equals(ADMIN)) {
			throw new ForbiddenException(FORBIDDEN_ACCESS);
		}
	}

	private void checkCommentIsDeleted(CommentEntity comment) {
		if (comment.getIsDeleted()) {
			throw new DuplicateException(COMMENT_ISDELETED);
		}
	}
}
