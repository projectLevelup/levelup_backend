package com.sparta.levelup_backend.exception.user;

import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.enums.ErrorCode;

public class AlreadyDeletedUserException extends BusinessException {

    public AlreadyDeletedUserException() {
        super(ErrorCode.ALREADY_DELETED_USER);
    }
}
