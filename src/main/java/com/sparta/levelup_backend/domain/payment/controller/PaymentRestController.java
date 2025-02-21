package com.sparta.levelup_backend.domain.payment.controller;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.payment.dto.request.CancelPaymentRequestDto;
import com.sparta.levelup_backend.domain.payment.dto.response.CancelResponseDto;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.levelup_backend.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentService paymentService;

    @PostMapping("/v3/request/{orderId}")
    public ApiResponse<PaymentResponseDto> createPayment(
            @AuthenticationPrincipal CustomUserDetails auth,
            @PathVariable Long orderId
    ) {
        PaymentResponseDto response = paymentService.createPayment(auth, orderId);
        return success(OK, OK_REQUEST, response);
    }

    @PostMapping("v3/request/cancel")
    public ApiResponse<CancelResponseDto> requestCancel(
            @AuthenticationPrincipal CustomUserDetails auth,
            @RequestBody CancelPaymentRequestDto dto
    ) {
        CancelResponseDto response = paymentService.requestCancel(auth, dto);
        return success(OK, OK_REQUEST_CANCEL, response);
    }
}
