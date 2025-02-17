package com.sparta.levelup_backend.domain.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.levelup_backend.domain.community.entity.CommunityEntity;

public interface CommunityRepository extends JpaRepository<CommunityEntity, Long> {
	Page<CommunityEntity> findAllByIsDeletedFalse(Pageable pageable);
}
