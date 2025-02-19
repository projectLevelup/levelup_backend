package com.sparta.levelup_backend.domain.review.repositoryES;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.review.document.ReviewDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

@Repository
public class ReviewESRepository {

	private final ElasticsearchClient elasticsearchClient;

	public ReviewESRepository(ElasticsearchClient elasticsearchClient) {
		this.elasticsearchClient = elasticsearchClient;
	}

	public List<ReviewDocument> findByProductId(Long productId) {
		return List.of();
	}

	public ElasticsearchClient getElasticsearchClient() {
		return elasticsearchClient;
	}
}
