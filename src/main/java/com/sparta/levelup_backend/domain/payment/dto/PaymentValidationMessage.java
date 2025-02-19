package com.sparta.levelup_backend.domain.payment.dto;

public class PaymentValidationMessage {
    public static final String PAYMENT_BLANK_MESSAGE ="paymentKey는 필수 값입니다.";
    public static final String PAYMENT_MAX_MESSAGE = "paymentKey는 최대 200자까지 가능합니다.";
    public static final String ORDER_BLANK_MESSAGE = "orderId는 필수 값입니다.";
    public static final String ORDER_MAX_MESSAGE = "orderId는 6자 이상 64자 이하로 입력해야합니다.";
    public static final String AMOUNT_MIN_MESSAGE = "amount는 1 이상이어야 합니다.";

}
