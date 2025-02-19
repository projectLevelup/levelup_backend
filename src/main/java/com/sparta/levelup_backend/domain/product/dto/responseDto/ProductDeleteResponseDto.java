package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;

import lombok.Getter;

@Getter
public class ProductDeleteResponseDto {

	private final Long productId;
	private final String productName;

	public ProductDeleteResponseDto(ProductDocument document) {
		this.productId = document.getProductId();
		this.productName = document.getProductName();
	}
}
