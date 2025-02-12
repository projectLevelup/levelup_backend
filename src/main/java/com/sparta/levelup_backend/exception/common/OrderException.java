package com.sparta.levelup_backend.exception.common;

public class OrderException extends BusinessException {
    public OrderException(ErrorCode errorCode) {
        super(ErrorCode.DUPLICATE_CANCELED_ORDER);
    }
}
