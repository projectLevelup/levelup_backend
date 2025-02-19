package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;

import lombok.Getter;

@Getter
public class ProductCreateResponseDto {

	private final Long id;
	private final String productName;

	// ✅ 기존 `ProductEntity` 기반 생성자
	public ProductCreateResponseDto(ProductEntity product) {
		this.id = product.getId(); // JPA 엔티티에서 ID 그대로 사용
		this.productName = product.getProductName();
	}

	// ✅ `ProductDocument` 기반 생성자 (Elasticsearch에서 검색된 데이터 변환)
	public ProductCreateResponseDto(ProductDocument document) {
		this.id = document.getProductId(); // ✅ Elasticsearch의 `_id`(String) 대신 productId(Long) 사용
		this.productName = document.getProductName();
	}
}
