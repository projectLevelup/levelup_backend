package com.sparta.levelup_backend.exception.common;

public class ForbiddenAccessException extends BusinessException {

    public ForbiddenAccessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ForbiddenAccessException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
