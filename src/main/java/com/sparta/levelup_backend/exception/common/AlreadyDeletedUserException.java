package com.sparta.levelup_backend.exception.common;

public class AlreadyDeletedUserException extends BusinessException {

    public AlreadyDeletedUserException() {
        super(ErrorCode.ALREADY_DELETED_USER);
    }
}
