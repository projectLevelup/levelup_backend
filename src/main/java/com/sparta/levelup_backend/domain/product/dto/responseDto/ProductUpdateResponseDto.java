package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;

import lombok.Getter;

@Getter
public class ProductUpdateResponseDto {

	private final Long id;
	private final String productName;

	public ProductUpdateResponseDto(ProductEntity product) {
		this.id = product.getId();
		this.productName = product.getProductName();
	}
}
