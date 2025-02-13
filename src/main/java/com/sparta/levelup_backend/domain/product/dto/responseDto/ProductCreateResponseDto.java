package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCreateResponseDto {
	private final Long id;
	private final String productName;

	public ProductCreateResponseDto(ProductEntity product) {
		this.id = product.getId();
		this.productName = product.getProductName();
	}
}
