package com.sparta.levelup_backend.domain.product.dto.requestDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestAllDto {
	private Long productId;
	private String productName;
	private String contents;
	private Long price;
	private Integer amount;
	private String status;
	private String imgUrl;
	private Double sentimentScore;

	// ✅ fromDocument 메서드 추가 (Elasticsearch → DTO 변환)
	public static ProductRequestAllDto fromDocument(ProductDocument document) {
		return new ProductRequestAllDto(
			document.getProductId(),
			document.getProductName(),
			document.getContents(),
			document.getPrice(),
			document.getAmount(),
			document.getStatus().name(),
			document.getImgUrl(),
			document.getSentimentScore()
		);
	}
}

