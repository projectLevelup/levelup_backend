package com.sparta.levelup_backend.domain.product.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.utill.ProductStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "product", createIndex = true)
public class ProductDocument {

	@Id
	private String id;

	@Field(type = FieldType.Long)
	private Long productId;

	@Field(type = FieldType.Text, analyzer = "standard") // standard : 공백으로 기준 형태소 분리
	private String productName;

	@Field(type = FieldType.Text)
	private String contents;

	@Field(type = FieldType.Long)
	private Long price;

	@Field(type = FieldType.Integer)
	private Integer amount;

	@Field(type = FieldType.Keyword)
	private ProductStatus status;

	@Field(type = FieldType.Keyword)
	private String imgUrl;

	@Field(type = FieldType.Long)
	private Long userId;

	@Field(type = FieldType.Long)
	private Long gameId;

	@Field(type = FieldType.Double)
	private Double sentimentScore;

	@Field(type = FieldType.Keyword)
	private String gameGenre;

	@Field(type = FieldType.Boolean)
	private Boolean isDeleted;

	@Builder
	public ProductDocument(Long productId, String productName, String contents, Long price, Integer amount,
		ProductStatus status, String imgUrl, Long userId, Long gameId, String gameGenre, Double sentimentScore,
		Boolean isDeleted) {
		this.id = String.valueOf(productId);
		this.productId = productId;
		this.productName = productName;
		this.contents = contents;
		this.price = price;
		this.amount = amount;
		this.status = status;
		this.imgUrl = imgUrl;
		this.userId = userId;
		this.gameId = gameId;
		this.gameGenre = gameGenre;
		this.sentimentScore = sentimentScore;
		this.isDeleted = isDeleted;
	}

	// JPA의 ProductEntity → Elasticsearch의 ProductDocument 변환 메서드
	public static ProductDocument fromEntity(ProductEntity product) {
		return ProductDocument.builder()
			.productId(product.getId())
			.productName(product.getProductName())
			.contents(product.getContents())
			.price(product.getPrice())
			.amount(product.getAmount())
			.status(product.getStatus())
			.imgUrl(product.getImgUrl())
			.userId(product.getUser().getId())
			.gameId(product.getGame().getId())
			.gameGenre(product.getGame().getGenre())
			.sentimentScore(0.0)
			.isDeleted(product.getIsDeleted())
			.build();
	}

	public void updateIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
