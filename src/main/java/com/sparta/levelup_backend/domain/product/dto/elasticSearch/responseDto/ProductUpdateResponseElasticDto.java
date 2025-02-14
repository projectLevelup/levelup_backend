package com.sparta.levelup_backend.domain.product.dto.elasticSearch.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateResponseElasticDto {

	private Long id;
	private String productName;

	public ProductUpdateResponseElasticDto(ProductDocument product) {
		this.id = Long.parseLong(product.getId());
		this.productName = product.getProductName();
	}
}
