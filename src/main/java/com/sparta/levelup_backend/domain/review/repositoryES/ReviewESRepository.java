package com.sparta.levelup_backend.domain.review.repositoryES;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.review.document.ReviewDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;

@Repository
public class ReviewESRepository {

	private final ElasticsearchClient elasticsearchClient;

	public ReviewESRepository(ElasticsearchClient elasticsearchClient) {
		this.elasticsearchClient = elasticsearchClient;
	}

	/**
	 * Elasticsearch에서 특정 productId에 대한 평균 감성 점수를 계산
	 */
	public Double getAverageSentimentByProductId(Long productId) {
		try {
			SearchRequest searchRequest = SearchRequest.of(s -> s
				.index("reviews") // review 인덱스에서 검색
				.size(0) // 문서 반환 없이 Aggregation만 수행
				.query(q -> q
					.bool(b -> b
						.filter(f -> f.term(t -> t.field("productId").value(productId)))
					)
				)
				.aggregations("avg_sentiment", a -> a
					.avg(avg -> avg.field("sentimentScore"))
				)
			);

			// Elasticsearch 요청 실행
			SearchResponse<Void> searchResponse = elasticsearchClient.search(searchRequest, Void.class);
			Aggregate avgSentimentAgg = searchResponse.aggregations().get("avg_sentiment");

			return avgSentimentAgg.avg().value();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<ReviewDocument> findByProductId(Long productId) {
		return List.of();
	}
}
