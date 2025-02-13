package com.sparta.levelup_backend.domain.order.controller;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.auth.controller.AuthController;
import com.sparta.levelup_backend.domain.order.dto.requestDto.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.levelup_backend.domain.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    public ApiResponse<OrderResponseDto> createOrder(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody OrderCreateRequestDto dto
    ) {
        Long userId = authUser.getId();
        OrderResponseDto orderResponseDto = orderService.createOrder(userId, dto);
        return success(OK, ORDER_CREATE, orderResponseDto);
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponseDto> findOrder(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long orderId
    ) {
        Long userId = authUser.getId();
        OrderResponseDto orderById = orderService.findOrder(userId, orderId);
        return success(OK, ORDER_FIND, orderById);
    }

    // 주문 결제 완료
    @PatchMapping("/{orderId}")
    public ApiResponse<OrderResponseDto> updateOrder(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long orderId
    ) {
        Long userId = authUser.getId();
        OrderResponseDto order = orderService.updateOrder(userId, orderId);
        return success(OK, ORDER_UPDATE, order);
    }

    // 결제 완료
    @PatchMapping("/student/{orderId}")
    public ApiResponse<OrderResponseDto> completeOrder(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long orderId
    ) {
        Long userId = authUser.getId();
        OrderResponseDto order = orderService.completeOrder(userId, orderId);
        return success(OK, ORDER_COMPLETE, order);
    }

    // 주문 취소
    @DeleteMapping("/{orderId}")
    public ApiResponse<Void> deleteOrderByPending(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long orderId
    ) {
        Long userId = authUser.getId();
        orderService.deleteOrderByPending(userId, orderId);
        return success(OK, ORDER_CANCLED);
    }

    // 결제 취소 (거래중 일때)
    @DeleteMapping("/tutor/{orderId}")
    public ApiResponse<Void> deleteOrderByTrading(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long orderId
    ) {
        Long userId = authUser.getId();
        orderService.deleteOrderByTrading(userId, orderId);
        return success(OK, ORDER_CANCLED);
    }
}
