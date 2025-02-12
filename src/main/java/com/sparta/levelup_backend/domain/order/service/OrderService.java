package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.utill.OrderStatus;

public interface OrderService {
    OrderResponseDto orderCreate(Long userId, OrderCreateRequestDto dto);

    OrderResponseDto findOrder(Long userId, Long orderId);

    OrderResponseDto orderUpdate(Long userId, Long orderId);

    OrderResponseDto orderComplete(Long userId, Long orderId);

    Void deleteOrderByPending(Long userId, Long orderId);

    Void deleteOrderByTrading(Long userId, Long orderId);
}
