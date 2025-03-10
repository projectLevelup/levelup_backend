package com.sparta.levelup_backend.domain.order.controller;

import com.sparta.levelup_backend.common.apiresponse.ApiResponse;
import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.levelup_backend.domain.order.dto.response.OrderResponseDto;
import com.sparta.levelup_backend.domain.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.ORDER_CANCLED;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.success;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    public ApiResponse<OrderResponseDto> createOrder(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody OrderCreateRequestDto dto
    ) {
        log.info("userId: {}", authUser.getId());
        Long userId = authUser.getId();
        return success(OK, ORDER_CREATE, orderService.createOrder(userId, dto));
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponseDto> findOrder(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long orderId
    ) {
        Long userId = authUser.getId();
        return success(OK, ORDER_FIND, orderService.findOrder(userId, orderId));
    }

    // 주문 결제 완료
    @PatchMapping("/{orderId}")
    public ApiResponse<OrderResponseDto> updateOrder(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long orderId
    ) {
        Long userId = authUser.getId();
        return success(OK, ORDER_UPDATE, orderService.updateOrder(userId, orderId));
    }

    // 결제 완료
    @PatchMapping("/student/{orderId}")
    public ApiResponse<OrderResponseDto> completeOrder(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long orderId
    ) {
        Long userId = authUser.getId();
        return success(OK, ORDER_COMPLETE, orderService.completeOrder(userId, orderId));
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
