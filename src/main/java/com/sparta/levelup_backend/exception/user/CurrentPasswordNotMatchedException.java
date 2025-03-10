package com.sparta.levelup_backend.exception.user;

import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;

public class CurrentPasswordNotMatchedException extends BusinessException {

    public CurrentPasswordNotMatchedException() {
        super(ErrorCode.INVALID_CURRENT_PASSWORD);
    }
}
