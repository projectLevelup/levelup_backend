package com.sparta.levelup_backend.domain.product.dto.elasticSearch.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestElasticDto {

	private Long productId;

	public ProductRequestElasticDto(String productId) {
		this.productId = Long.parseLong(productId);
	}
}
