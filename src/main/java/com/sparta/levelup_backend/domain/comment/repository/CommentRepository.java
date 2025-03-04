package com.sparta.levelup_backend.domain.comment.repository;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.comment.entity.CommentEntity;
import com.sparta.levelup_backend.exception.common.NotFoundException;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	default CommentEntity findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));
	}

	List<CommentEntity> findByCommunityIdAndIsDeletedFalse(Long communityId);
}
