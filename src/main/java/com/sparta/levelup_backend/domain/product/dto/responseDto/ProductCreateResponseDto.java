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
		this.id = product.getId();
		this.productName = product.getProductName();
	}

	// ✅ 새롭게 추가된 `ProductDocument` 기반 생성자
	public ProductCreateResponseDto(ProductDocument document) {
		this.id = Long.parseLong(document.getId()); // Elasticsearch ID 변환
		this.productName = document.getProductName();
	}
}
