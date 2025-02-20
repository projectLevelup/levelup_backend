package com.sparta.levelup_backend.domain.product.dto.requestDto;

import static com.sparta.levelup_backend.domain.product.dto.ProductValidMessage.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductUpdateRequestDto {
	
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

	private final String status;

	private final String imgUrl;

}
