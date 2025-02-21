package com.sparta.levelup_backend.domain.payment.service;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto createPayment(CustomUserDetails auth, Long orderId);

    void updatePayment(String paymentKey, String approvedAt, String method, String orderId);
}
