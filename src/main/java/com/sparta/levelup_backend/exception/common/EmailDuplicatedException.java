package com.sparta.levelup_backend.exception.common;

public class EmailDuplicatedException extends BusinessException {

    public EmailDuplicatedException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
