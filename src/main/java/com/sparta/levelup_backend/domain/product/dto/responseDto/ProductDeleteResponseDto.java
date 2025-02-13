package com.sparta.levelup_backend.domain.product.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDeleteResponseDto {

	private final Long id;
	private final String productName;
}
