package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.utill.OrderStatus;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, OrderCreateRequestDto dto);

    OrderResponseDto findOrder(Long userId, Long orderId);

    OrderResponseDto updateOrder(Long userId, Long orderId);

    OrderResponseDto completeOrder(Long userId, Long orderId);

    Void deleteOrderByPending(Long userId, Long orderId);

    Void deleteOrderByTrading(Long userId, Long orderId);
}
