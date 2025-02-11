package com.sparta.levelup_backend.exception.common;

public class ProductOutOfAmount extends BusinessException {
    public ProductOutOfAmount() {
        super(ErrorCode.DUPLICATE_OUT_OF_AMOUNT);
    }
}
