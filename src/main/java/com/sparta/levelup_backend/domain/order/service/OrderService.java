package com.sparta.levelup_backend.domain.order.service;

import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderCreateResponseDto;

public interface OrderService {
    OrderCreateResponseDto orderCreate(OrderCreateRequestDto dto);
}
