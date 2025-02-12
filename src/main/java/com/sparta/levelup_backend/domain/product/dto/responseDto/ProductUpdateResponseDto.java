package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateResponseDto {

	private Long id;
	private String productName;

	public ProductUpdateResponseDto(ProductEntity product) {
		this.id = product.getId();
		this.productName = product.getProductName();
	}
}
