package com.sparta.levelup_backend.exception.user;

import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.enums.ErrorCode;

public class PasswordIncorrectException extends BusinessException {
  
    public PasswordIncorrectException() {
        super(ErrorCode.PASSWORD_INCORRECT);
    }
}
