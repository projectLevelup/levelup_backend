package com.sparta.levelup_backend.domain.order.controller;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.domain.order.service.OrderServiceImpl;
import com.sparta.levelup_backend.utill.OrderStatus;
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
    public ApiResponse<OrderResponseDto> orderCreate(
            @RequestBody OrderCreateRequestDto dto
    ) {
        OrderResponseDto orderResponseDto = orderService.orderCreate(dto);
        return success(HttpStatus.OK, ORDER_CREATE, orderResponseDto);
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponseDto> findOrder(
            @PathVariable Long orderId
    ) {
        OrderResponseDto orderById = orderService.findOrder(orderId);
        return success(HttpStatus.OK, ORDER_FIND, orderById);
    }

//    // 주문 수정
//    @PatchMapping("/{orderId}")
//    public ApiResponse<OrderResponseDto> orderUpdate(
//            @PathVariable Long orderId,
//            @RequestParam OrderStatus status
//    ) {
//        OrderResponseDto order = orderService.orderUpdate(orderId, status);
//    }
}
