package com.sparta.levelup_backend.domain.product.dto.elasticSearch.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;

import lombok.Getter;

@Getter
public class ProductCreateResponseElasticDto {
	private final Long id;
	private final String productName;

	public ProductCreateResponseElasticDto(ProductDocument product) {
		this.id = Long.parseLong(product.getId());
		this.productName = product.getProductName();
	}
}
