package com.sparta.levelup_backend.domain.community.repository;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;
import com.sparta.levelup_backend.exception.common.NotFoundException;

public interface CommunityRepository extends JpaRepository<CommunityEntity, Long> {
	default CommunityEntity findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new NotFoundException(COMMUNITY_NOT_FOUND));
	}

	Page<CommunityEntity> findAllByGameNameAndTitleContainingAndIsDeletedFalse(String gameName, String title, Pageable pageable);

	Page<CommunityEntity> findAllByIsDeletedFalse(Pageable pageable);
}
