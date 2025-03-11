package com.sparta.levelup_backend.domain.community.repository;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.exception.community.CommunityException;

public interface CommunityRepository extends JpaRepository<CommunityEntity, Long> {
	default CommunityEntity findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new CommunityException(COMMUNITY_NOT_FOUND));
	}
}
