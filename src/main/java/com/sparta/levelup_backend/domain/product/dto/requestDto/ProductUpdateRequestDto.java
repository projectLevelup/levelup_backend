package com.sparta.levelup_backend.domain.product.dto.requestDto;

import com.sparta.levelup_backend.utill.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestDto {

	@NotBlank
	private String productName; // 수정할 상품 이름

	@NotBlank
	private String contents; // 수정할 상품 설명

	@NotNull
	private Long price; // 수정할 가격

	@NotNull
	private Integer amount; // 수정할 수량

	@NotNull
	private ProductStatus status; // 수정할 상태 (예: ACTIVE, INACTIVE)
}
