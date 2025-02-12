package com.sparta.levelup_backend.domain.order.dto.responseDto;

import com.sparta.levelup_backend.utill.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {

    private final Long orderId;

    private final Long productId;

    private final String productName;

    private final OrderStatus status;

    private final Long price;
}
