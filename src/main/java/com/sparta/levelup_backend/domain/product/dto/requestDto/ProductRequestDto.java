package com.sparta.levelup_backend.domain.product.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 🔥 기본 생성자 추가 (Jackson이 객체 생성 가능하도록)
public class ProductRequestDto {

	private Long productId;

	@JsonCreator
	public ProductRequestDto(@JsonProperty("productId") Long productId) {
		this.productId = productId;
	}
}
