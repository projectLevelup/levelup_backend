package com.sparta.levelup_backend.exception.common;

public class CurrentPasswordNotMatchedException extends BusinessException {

    public CurrentPasswordNotMatchedException() {
        super(ErrorCode.INVALID_CURRENT_PASSWORD);
    }
}
