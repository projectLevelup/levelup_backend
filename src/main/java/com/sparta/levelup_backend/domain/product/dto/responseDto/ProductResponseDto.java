package com.sparta.levelup_backend.domain.product.dto.responseDto;

import com.sparta.levelup_backend.utill.ProductStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductResponseDto {

    private final Long userId; // 판매자 ID
    private final Long gameId; // 게임 ID
    private final String productName;
    private final String contents;
    private final Long price;
    private final Integer amount;
    private final ProductStatus status;
    private final String imgUrl;
}

