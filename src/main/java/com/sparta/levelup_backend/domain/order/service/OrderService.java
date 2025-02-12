package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto orderCreate(OrderCreateRequestDto dto);

    OrderResponseDto findOrder(Long orderId);
}
