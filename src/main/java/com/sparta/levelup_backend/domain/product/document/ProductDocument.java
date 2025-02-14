package com.sparta.levelup_backend.domain.product.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.utill.ProductStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(indexName = "product") // ✅ Elasticsearch 인덱스 지정
public class ProductDocument {

	@Id
	private String id; // Elasticsearch는 ID가 String 타입이어야 함

	@Field(type = FieldType.Text)
	private String productName;

	@Field(type = FieldType.Text)
	private String contents;

	@Field(type = FieldType.Long)
	private Long price;

	@Field(type = FieldType.Integer)
	private Integer amount;

	@Field(type = FieldType.Keyword)
	private ProductStatus status;

	@Field(type = FieldType.Text)
	private String imgUrl;

	@Field(type = FieldType.Long)
	private Long userId;

	@Field(type = FieldType.Long)
	private Long gameId;

	// ✅ JPA의 BaseEntity에서 상속받을 필드 직접 추가
	@Field(type = FieldType.Boolean)
	private Boolean isDeleted = false;  // 기본값 false

	@Field(type = FieldType.Date)
	private LocalDateTime createdAt;  // 생성일 (Elasticsearch에서 직접 관리)

	@Field(type = FieldType.Date)
	private LocalDateTime updatedAt;  // 수정일 (Elasticsearch에서 직접 관리)

	@Builder
	public ProductDocument(String id, String productName, String contents, Long price, Integer amount,
		ProductStatus status, String imgUrl, Long userId, Long gameId,
		Boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.productName = productName;
		this.contents = contents;
		this.price = price;
		this.amount = amount;
		this.status = status;
		this.imgUrl = imgUrl;
		this.userId = userId;
		this.gameId = gameId;
		this.isDeleted = (isDeleted != null) ? isDeleted : false; // 기본값 설정
		this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now(); // 생성 날짜 자동 설정
		this.updatedAt = (updatedAt != null) ? updatedAt : LocalDateTime.now(); // 업데이트 날짜 자동 설정
	}

	// ✅ JPA의 ProductEntity → Elasticsearch의 ProductDocument 변환 메서드 추가
	public static ProductDocument fromEntity(ProductEntity product) {
		return ProductDocument.builder()
			.id(String.valueOf(product.getId()))
			.productName(product.getProductName())
			.contents(product.getContents())
			.price(product.getPrice())
			.amount(product.getAmount())
			.status(product.getStatus())
			.imgUrl(product.getImgUrl())
			.userId(product.getUser().getId())
			.gameId(product.getGame().getId())
			.isDeleted(product.getIsDeleted())  // ✅ 기본값 false 유지
			.createdAt(product.getCreatedAt() != null ? product.getCreatedAt() : LocalDateTime.now())  // ✅ 자동 생성
			.updatedAt(product.getUpdatedAt() != null ? product.getUpdatedAt() : LocalDateTime.now())  // ✅ 자동 업데이트
			.build();
	}

	// ✅ Elasticsearch 문서를 업데이트할 때 날짜 자동 갱신
	public ProductDocument update(ProductEntity product) {
		this.productName = product.getProductName();
		this.contents = product.getContents();
		this.price = product.getPrice();
		this.amount = product.getAmount();
		this.status = product.getStatus();
		this.imgUrl = product.getImgUrl();
		this.userId = product.getUser().getId();
		this.gameId = product.getGame().getId();
		this.isDeleted = product.getIsDeleted();
		this.updatedAt = LocalDateTime.now();  // ✅ 수정될 때마다 자동 업데이트

		return this;
	}

	public Long getLongId() {
		return Long.parseLong(this.id);
	}

}
