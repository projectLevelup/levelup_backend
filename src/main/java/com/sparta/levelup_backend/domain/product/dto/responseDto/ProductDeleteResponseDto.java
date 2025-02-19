package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;

import lombok.Getter;

@Getter
public class ProductDeleteResponseDto {

	private final Long id;
	private final String productName;

	// ✅ JPA `ProductEntity` 기반 생성자
	public ProductDeleteResponseDto(ProductEntity product) {
		this.id = product.getId(); // JPA 엔티티의 ID
		this.productName = product.getProductName();
	}

	// ✅ Elasticsearch `ProductDocument` 기반 생성자
	public ProductDeleteResponseDto(ProductDocument document) {
		this.id = document.getProductId(); // Elasticsearch 상품 ID (Long)
		this.productName = document.getProductName();
	}
}
