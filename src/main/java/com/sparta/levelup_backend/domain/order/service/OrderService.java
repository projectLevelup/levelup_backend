package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.response.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, OrderCreateRequestDto dto);

    OrderResponseDto findOrder(Long userId, Long orderId);

    OrderResponseDto updateOrder(Long userId, Long orderId);

    OrderResponseDto completeOrder(Long userId, Long orderId);

    void deleteOrderByPending(Long userId, Long orderId);

    void deleteOrderByTrading(Long userId, Long orderId);

    Page<OrderResponseDto> findStudentOrders(Long userId, Pageable pageable);

    Page<OrderResponseDto> findTutorOrders(Long tutorId, Pageable pageable);
}
