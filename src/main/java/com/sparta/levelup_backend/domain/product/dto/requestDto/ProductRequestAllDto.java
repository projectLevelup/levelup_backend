package com.sparta.levelup_backend.domain.product.dto.requestDto;

import static com.sparta.levelup_backend.domain.product.dto.ProductValidMessage.*;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProductRequestAllDto {

	@NotNull(message = PRODUCT_NAME_REQUIRED)
	@Size(max = 255, message = PRODUCT_NAME_LENGTH)
	private final String productName;

	@NotNull(message = CONTENTS_REQUIRED)
	@Size(max = 1000, message = CONTENTS_LENGTH)
	private final String contents;

	@NotNull(message = PRICE_REQUIRED)
	private final Long price;

	@NotNull(message = AMOUNT_REQUIRED)
	private final Integer amount;

	@NotNull(message = STATUS_REQUIRED)
	private final String status;

	private final String imgUrl;

	private final Double sentimentScore;

	public static ProductRequestAllDto fromDocument(ProductDocument document) {
		return new ProductRequestAllDto(
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

