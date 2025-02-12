package com.sparta.levelup_backend.domain.order.controller;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.domain.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> orderCreate(
            @RequestBody OrderCreateRequestDto dto
    ) {
        OrderResponseDto orderResponseDto = orderService.orderCreate(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(success(
                        ORDER_CREATE,
                        orderResponseDto
                ));
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> findOrder(
            @PathVariable Long orderId
    ) {
        OrderResponseDto orderById = orderService.findOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(success(
                        ORDER_FIND,
                        orderById
                ));
    }
}
