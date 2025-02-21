package com.sparta.levelup_backend.domain.payment.dto.response;

import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PaymentResponseDto {

    private Long amount;

    private String orderName;

    private String orderId;

    private String customerEmail;

    private String customerName;

    private String customerKey;

    @Setter
    private String SuccessUrl;

    @Setter
    private String failUrl;

    public PaymentResponseDto(PaymentEntity payment) {
        this.amount = payment.getAmount();
        this.orderName = payment.getOrder().getOrderName();
        this.orderId = payment.getOrderId();
        this.customerEmail = payment.getOrder().getUser().getEmail();
        this.customerName = payment.getOrder().getUser().getNickName();
        this.customerKey = payment.getUserKey();
    }
}
