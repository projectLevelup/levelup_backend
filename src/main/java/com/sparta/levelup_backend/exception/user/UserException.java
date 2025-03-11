package com.sparta.levelup_backend.exception.user;

import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.common.BusinessException;

public class UserException extends BusinessException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
