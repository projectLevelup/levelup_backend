package com.sparta.levelup_backend.domain.review.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "reviews", createIndex = true) // ES 인덱스명 지정
public class ReviewDocument {

	@Id
	private Long id; // 리뷰 ID (ES 기본 식별자)
	private Long productId; // 상품 ID
	private String contents; // 리뷰 내용
	private double sentimentScore; // 감성 점수 (사전 계산된 값 저장)

	// 기본 생성자
	public ReviewDocument() {
	}

	// 생성자
	public ReviewDocument(Long id, Long productId, String contents, double sentimentScore) {
		this.id = id;
		this.productId = productId;
		this.contents = contents;
		this.sentimentScore = sentimentScore;
	}

	// Getter & Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public double getSentimentScore() {
		return sentimentScore;
	}

	public void setSentimentScore(double sentimentScore) {
		this.sentimentScore = sentimentScore;
	}
}
