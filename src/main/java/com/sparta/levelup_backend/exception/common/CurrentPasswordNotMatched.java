package com.sparta.levelup_backend.exception.common;

public class CurrentPasswordNotMatched extends BusinessException {

    public CurrentPasswordNotMatched() {
        super(ErrorCode.INVALID_CURRENT_PASSWORD);
    }
}
