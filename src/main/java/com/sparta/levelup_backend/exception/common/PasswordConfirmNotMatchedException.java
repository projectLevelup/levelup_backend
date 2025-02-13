package com.sparta.levelup_backend.exception.common;

public class PasswordConfirmNotMatchedException extends BusinessException {

    public PasswordConfirmNotMatchedException() {
        super(ErrorCode.INVALID_PASSWORD_CONFIRM);
    }
}
