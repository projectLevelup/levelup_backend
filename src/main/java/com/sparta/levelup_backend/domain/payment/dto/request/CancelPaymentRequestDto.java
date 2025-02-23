package com.sparta.levelup_backend.domain.payment.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CancelPaymentRequestDto {
    private final String key;
    private final String reason;
}
