package com.sparta.levelup_backend.exception.common;

public class PasswordConfirmNotMatched extends BusinessException {

    public PasswordConfirmNotMatched() {
        super(ErrorCode.INVALID_PASSWORD_CONFIRM);
    }
}
