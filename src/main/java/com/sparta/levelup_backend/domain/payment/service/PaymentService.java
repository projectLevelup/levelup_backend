package com.sparta.levelup_backend.domain.payment.service;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.payment.dto.request.CancelPaymentRequestDto;
import com.sparta.levelup_backend.domain.payment.dto.response.CancelResponseDto;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto createPayment(CustomUserDetails auth, Long orderId);

    CancelResponseDto requestCancel(CustomUserDetails auth, CancelPaymentRequestDto dto);
}
