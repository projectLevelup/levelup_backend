package com.sparta.levelup_backend.domain.product.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDeleteResponseDto {

	private final Long id; // 수정된 상품의 ID
	private final String productName; // 수정된 상품 이름
}
