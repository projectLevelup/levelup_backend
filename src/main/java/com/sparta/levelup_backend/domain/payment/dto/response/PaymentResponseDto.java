package com.sparta.levelup_backend.domain.payment.dto.response;

import com.sparta.levelup_backend.utill.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private String payType;

    private Long amount;

    private String orderId;

    private String orderName;

    private String customerEmail;

    private String customerName;

    @Setter
    private String successUrl;

    @Setter
    private String failUrl;

    private LocalDateTime createDate;

    private String paySuccessYn;
}
