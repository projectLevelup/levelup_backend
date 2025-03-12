package com.sparta.levelup_backend.domain.review.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "reviews", createIndex = true)
public class ReviewDocument {

	@Id
	private Long id; // 리뷰 ID (ES 기본 식별자)

	@Field(type = FieldType.Long)
	private Long productId; // 상품 ID

	@Field(type = FieldType.Text, analyzer = "standard")
	private String contents; // 리뷰 내용

	@Field(type = FieldType.Double) // Keyword → Double로 변경
	private double sentimentScore; // 감성 점수 (사전 계산된 값 저장)

}
