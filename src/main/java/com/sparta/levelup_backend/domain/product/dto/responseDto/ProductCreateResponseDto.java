package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;

import lombok.Getter;

@Getter
public class ProductCreateResponseDto {

	private final Long productId;
	private final String productName;

	public ProductCreateResponseDto(ProductDocument document) {
		this.productId = document.getProductId();
		this.productName = document.getProductName();
	}
}
