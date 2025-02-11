package com.sparta.levelup_backend.domain.review.repository;

import com.sparta.levelup_backend.domain.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
