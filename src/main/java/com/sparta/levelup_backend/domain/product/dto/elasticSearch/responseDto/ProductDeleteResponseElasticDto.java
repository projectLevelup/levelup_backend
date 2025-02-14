package com.sparta.levelup_backend.domain.product.dto.elasticSearch.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;

import lombok.Getter;

@Getter
public class ProductDeleteResponseElasticDto {

	private final Long id;  // ✅ Elasticsearch의 ID는 String이므로 변환 필요
	private final String productName;

	// ✅ Elasticsearch의 `ProductDocument`를 기반으로 생성
	public ProductDeleteResponseElasticDto(ProductDocument document) {
		this.id = Long.parseLong(document.getId());  // ✅ String → Long 변환
		this.productName = document.getProductName();
	}
}
