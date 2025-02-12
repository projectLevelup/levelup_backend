package com.sparta.levelup_backend.domain.product.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDeleteResponseDto {

	private Long id; // 수정된 상품의 ID
	private String productName; // 수정된 상품 이름
}
