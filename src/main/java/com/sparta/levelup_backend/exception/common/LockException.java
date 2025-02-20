package com.sparta.levelup_backend.exception.common;

public class LockException extends BusinessException {
    public LockException(ErrorCode errorCode) {
        super(errorCode);
    }

    public LockException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
