package com.sparta.levelup_backend.domain.review.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;

@Getter
@Document(indexName = "review", createIndex = true) // ES 인덱스명 지정
public class ReviewDocument {

	@Id
	private String id; // 리뷰 ID (문자열)

	@Field(type = FieldType.Date)
	private String updatedAt; // 업데이트 날짜 (ISO 8601 형식)

	@Field(type = FieldType.Text, analyzer = "standard")
	private String contents; // 리뷰 내용

	@Field(type = FieldType.Integer)
	private Integer starScore; // 별점

	@Field(type = FieldType.Long)
	private Long productId; // 상품 ID

	@Field(type = FieldType.Long)
	private Long ordersId; // 주문 ID

	@Field(type = FieldType.Date)
	private String createdAt; // 생성 날짜 (ISO 8601 형식)

	@Field(type = FieldType.Long)
	private Long userId; // 사용자 ID

	@Field(type = FieldType.Boolean)
	private Boolean isDeleted; // 삭제 여부

	// 생성자
	public ReviewDocument(String id, String updatedAt, String contents, Integer starScore, Long productId,
		Long ordersId, String createdAt, Long userId, Boolean isDeleted) {
		this.id = String.valueOf(id);
		this.updatedAt = updatedAt;
		this.contents = contents;
		this.starScore = starScore;
		this.productId = productId;
		this.ordersId = ordersId;
		this.createdAt = createdAt;
		this.userId = userId;
		this.isDeleted = isDeleted;
	}
}
