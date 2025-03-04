package com.sparta.levelup_backend.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.comment.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
