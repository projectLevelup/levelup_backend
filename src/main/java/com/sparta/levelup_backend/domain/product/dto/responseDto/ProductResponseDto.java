package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.utill.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponseDto {

    private final Long userId; // 판매자 ID
    private final Long gameId; // 게임 ID
    private final String productName;
    private final String contents;
    private final Long price;
    private final Integer amount;
    private final ProductStatus status;
    private final String imgUrl;


    public ProductResponseDto(ProductEntity entity) {
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



