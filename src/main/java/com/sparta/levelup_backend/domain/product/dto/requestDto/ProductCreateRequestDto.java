package com.sparta.levelup_backend.domain.product.dto.requestDto;

import com.sparta.levelup_backend.utill.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {
	private Long userId;
	private Long gameId;
	private String productName;
	private String contents;
	private Long price;
	private Integer amount;
	private ProductStatus status;
	private String imgUrl;
}
