package com.sparta.levelup_backend.exception.user;

import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.enums.ErrorCode;

public class EmailDuplicatedException extends BusinessException {

    public EmailDuplicatedException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
