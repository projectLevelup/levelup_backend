package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
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

	// ✅ 기존 `ProductEntity` 기반 생성자
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

	// ✅ `ProductDocument` 기반 생성자 (Elasticsearch에서 검색된 데이터 변환)
	public ProductResponseDto(ProductDocument document) {
		this.id = document.getProductId(); // ✅ `_id` 대신 `productId`(Long) 사용
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
