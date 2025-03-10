package com.sparta.levelup_backend.exception.user;

import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.enums.ErrorCode;

public class PasswordConfirmNotMatchedException extends BusinessException {

    public PasswordConfirmNotMatchedException() {
        super(ErrorCode.INVALID_PASSWORD_CONFIRM);
    }
}
