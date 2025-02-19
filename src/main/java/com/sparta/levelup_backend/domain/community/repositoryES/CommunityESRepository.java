package com.sparta.levelup_backend.domain.community.repositoryES;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.community.document.CommunityDocument;

@Repository
public interface CommunityESRepository
	extends ElasticsearchRepository<CommunityDocument, String> {
	Page<CommunityDocument> findByTitleAndIsDeletedFalse(String searchKeyword, Pageable pageable);
}