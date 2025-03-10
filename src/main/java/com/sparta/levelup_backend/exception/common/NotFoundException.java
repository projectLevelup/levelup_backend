package com.sparta.levelup_backend.exception.common;

import com.sparta.levelup_backend.enums.ErrorCode;

public class NotFoundException extends BusinessException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
