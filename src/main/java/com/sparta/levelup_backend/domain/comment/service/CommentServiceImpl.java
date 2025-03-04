package com.sparta.levelup_backend.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.comment.dto.request.CommentRequestDto;
import com.sparta.levelup_backend.domain.comment.dto.response.CommentResponseDto;
import com.sparta.levelup_backend.domain.comment.entity.CommentEntity;
import com.sparta.levelup_backend.domain.comment.repository.CommentRepository;
import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.domain.community.repository.CommunityRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
	private final UserRepository userRepository;
	private final CommunityRepository communityRepository;
	private final CommentRepository commentRepository;

	@Override
	public CommentResponseDto saveComment(Long userId, CommentRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		CommunityEntity community = communityRepository.findByIdOrElseThrow(dto.getCommunityId());

		CommentEntity comment = commentRepository.save(new CommentEntity(dto.getContent(), user, community));
		return CommentResponseDto.from(comment);
	}
}
