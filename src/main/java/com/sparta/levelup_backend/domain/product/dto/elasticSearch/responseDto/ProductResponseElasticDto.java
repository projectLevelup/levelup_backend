package com.sparta.levelup_backend.domain.product.dto.elasticSearch.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.utill.ProductStatus;

import lombok.Getter;

@Getter
public class ProductResponseElasticDto {

	private final Long id;
	private final Long userId;
	private final Long gameId;
	private final String productName;
	private final String contents;
	private final Long price;
	private final Integer amount;
	private final ProductStatus status;
	private final String imgUrl;

	public ProductResponseElasticDto(ProductDocument document) {
		this.id = Long.parseLong(document.getId());
		this.userId = document.getUserId();
		this.gameId = document.getGameId();
		this.productName = document.getProductName();
		this.contents = document.getContents();
		this.price = document.getPrice();
		this.amount = document.getAmount();
		this.status = document.getStatus();
		this.imgUrl = document.getImgUrl();
	}
}
