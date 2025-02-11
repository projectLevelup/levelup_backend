package com.sparta.levelup_backend.domain.order.controller;

import com.sparta.levelup_backend.common.ApiResMessage;
import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderCreateResponseDto;
import com.sparta.levelup_backend.domain.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateResponseDto>> orderCreate(
            @RequestBody OrderCreateRequestDto dto
    ) {
        OrderCreateResponseDto orderCreateResponseDto = orderService.orderCreate(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        ApiResMessage.ORDER_CREATE,
                        orderCreateResponseDto
                ));
    }
}
