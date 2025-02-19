package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.utill.ProductStatus;

import lombok.Getter;

@Getter
public class ProductResponseDto {

	private final Long id;
	private final Long userId;
	private final Long gameId;
	private final String productName;
	private final String contents;
	private final Long price;
	private final Integer amount;
	private final ProductStatus status;
	private final String imgUrl;

	public ProductResponseDto(ProductEntity entity) {
		this.id = entity.getId();
		this.userId = entity.getUser().getId();
		this.gameId = entity.getGame().getId();
		this.productName = entity.getProductName();
		this.contents = entity.getContents();
		this.price = entity.getPrice();
		this.amount = entity.getAmount();
		this.status = entity.getStatus();
		this.imgUrl = entity.getImgUrl();
	}
}
