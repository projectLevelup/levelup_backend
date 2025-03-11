package com.sparta.levelup_backend.domain.community.repositoryes;

import static com.sparta.levelup_backend.enums.ErrorCode.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.community.document.CommunityDocument;
import com.sparta.levelup_backend.exception.community.CommunityException;

@Repository
public interface CommunityESRepository
	extends ElasticsearchRepository<CommunityDocument, String> {
	Page<CommunityDocument> findByTitleAndIsDeletedFalse(String searchKeyword, Pageable pageable);

	default CommunityDocument findByIdOrElseThrow(String communityId) {
		return findById(communityId).orElseThrow(() -> new CommunityException(COMMUNITY_NOT_FOUND));
	}
}