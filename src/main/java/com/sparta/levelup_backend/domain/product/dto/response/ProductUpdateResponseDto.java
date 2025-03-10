package com.sparta.levelup_backend.domain.product.dto.response;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;

import lombok.Getter;

@Getter
public class ProductUpdateResponseDto {

	private final Long productId;
	private final String productName;

	public ProductUpdateResponseDto(ProductDocument document) {
		this.productId = document.getProductId();
		this.productName = document.getProductName();
	}
}
