package com.sparta.levelup_backend.domain.order.controller;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.order.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateDto>> orderCreate
}
