package com.sparta.levelup_backend.domain.review.repositoryES;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sparta.levelup_backend.domain.review.document.ReviewDocument;

@Repository
public interface ReviewESRepository extends ElasticsearchRepository<ReviewDocument, String> {
	List<ReviewDocument> findByProductId(Long productId);

	// 또는 @Query를 사용해 직접 쿼리 작성 가능
	@Query("{ \"term\": { \"product_id\": ?0 } }")
	List<ReviewDocument> findReviewsByProductId(Long productId);
}

