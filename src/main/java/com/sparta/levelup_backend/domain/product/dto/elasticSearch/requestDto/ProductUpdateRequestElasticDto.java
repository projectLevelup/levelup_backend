package com.sparta.levelup_backend.domain.product.dto.elasticSearch.requestDto;

import static com.sparta.levelup_backend.domain.product.dto.ProductValidMessage.*;

import com.sparta.levelup_backend.utill.ProductStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestElasticDto {

	@NotNull(message = PRODUCT_NAME_REQUIRED)
	@Size(max = 255, message = PRODUCT_NAME_LENGTH)
	private String productName;

	@NotNull(message = CONTENTS_REQUIRED)
	@Size(max = 1000, message = CONTENTS_LENGTH)
	private String contents;

	@NotNull(message = PRICE_REQUIRED)
	private Long price;

	@NotNull(message = AMOUNT_REQUIRED)
	private Integer amount;

	@NotNull(message = STATUS_REQUIRED)
	private ProductStatus status;
}
