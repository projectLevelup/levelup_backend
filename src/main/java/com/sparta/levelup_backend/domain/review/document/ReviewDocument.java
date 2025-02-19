package com.sparta.levelup_backend.domain.review.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;

@Getter
@Document(indexName = "reviews", createIndex = true) // ES 인덱스명 지정
public class ReviewDocument {

	// Getter & Setter
	@Id
	private Long id; // 리뷰 ID (ES 기본 식별자)

	@Field(type = FieldType.Long)
	private Long productId; // 상품 ID

	@Field(type = FieldType.Text, analyzer = "standard")
	private String contents; // 리뷰 내용

	@Field(type = FieldType.Keyword)
	private double sentimentScore; // 감성 점수 (사전 계산된 값 저장)

	// 생성자
	public ReviewDocument(Long id, Long productId, String contents, double sentimentScore) {
		this.id = id;
		this.productId = productId;
		this.contents = contents;
		this.sentimentScore = sentimentScore;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
