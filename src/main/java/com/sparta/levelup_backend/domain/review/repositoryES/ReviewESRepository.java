package com.sparta.levelup_backend.domain.review.repositoryES;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.review.document.ReviewDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

@Repository
public class ReviewESRepository {

	private final ElasticsearchClient elasticsearchClient;

	public ReviewESRepository(ElasticsearchClient elasticsearchClient) {
		this.elasticsearchClient = elasticsearchClient;
	}

	public List<ReviewDocument> findByProductId(Long productId) {
		try {
			SearchResponse<ReviewDocument> response = elasticsearchClient.search(s -> s
					.index("review")
					.query(q -> q
						.match(m -> m
							.field("productId")
							.query(productId)
						)
					),
				ReviewDocument.class
			);

			return response.hits().hits().stream()
				.map(Hit::source)
				.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
			return List.of(); // 예외 발생 시 빈 리스트 반환
		}
	}

	public ElasticsearchClient getElasticsearchClient() {
		return elasticsearchClient;
	}
}
