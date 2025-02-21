package com.sparta.levelup_backend.domain.payment.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CancelPaymentResponseDto {
    private final String paymentKey;
    private final String orderId;
    private final String status;
    private final String canceledAt;
    private final Long cancelAmount;
}
