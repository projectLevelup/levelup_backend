package com.sparta.levelup_backend.exception.order;

import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.enums.ErrorCode;

public class OrderException extends BusinessException {
    public OrderException(ErrorCode errorCode) {
        super(errorCode);
    }
}
