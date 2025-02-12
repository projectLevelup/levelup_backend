package com.sparta.levelup_backend.domain.order.dto.requestDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderCreateRequestDto {

    private final Long productId;
}
