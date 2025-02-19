package com.sparta.levelup_backend.domain.product.dto.requestDto;

import static com.sparta.levelup_backend.domain.product.dto.ProductValidMessage.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.levelup_backend.utill.ProductStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 기본 생성자 추가 (Jackson이 객체 생성 가능하도록)
public class ProductUpdateRequestDto {

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

	@JsonCreator
	public ProductUpdateRequestDto(
		@JsonProperty("productName") String productName,
		@JsonProperty("contents") String contents,
		@JsonProperty("price") Long price,
		@JsonProperty("amount") Integer amount,
		@JsonProperty("status") ProductStatus status) {
		this.productName = productName;
		this.contents = contents;
		this.price = price;
		this.amount = amount;
		this.status = status;
	}
}
