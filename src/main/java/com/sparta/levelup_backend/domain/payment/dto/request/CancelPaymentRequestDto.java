package com.sparta.levelup_backend.domain.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class CancelPaymentRequestDto {
    private final String key;
    private final String reason;
}
