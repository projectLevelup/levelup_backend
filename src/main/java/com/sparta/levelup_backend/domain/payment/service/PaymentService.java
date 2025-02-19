package com.sparta.levelup_backend.domain.payment.service;

import com.sparta.levelup_backend.domain.payment.dto.request.PaymentRequestDto;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto requestPayments(String customerKey, PaymentRequestDto paymentRequestDto);
}
