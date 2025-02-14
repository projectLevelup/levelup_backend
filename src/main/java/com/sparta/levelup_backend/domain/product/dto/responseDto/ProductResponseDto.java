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

	// ✅ 기존 `ProductEntity`를 이용한 생성자
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

	// ✅ 새로운 `ProductDocument`를 이용한 생성자 추가
	public ProductResponseDto(ProductDocument document) {
		this.id = Long.parseLong(document.getId()); // Elasticsearch의 ID는 String → Long 변환 필요
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
