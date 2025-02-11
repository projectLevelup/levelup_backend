package com.sparta.levelup_backend.common;

import com.sparta.levelup_backend.exception.common.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String errorCode;
    private final String errorMessage;
    private final String detail;

    private ErrorResponse(ErrorCode errorCode, String detail) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
        this.detail = detail;
    }

    public static ErrorResponse of(ErrorCode errorCode, String detail) {
        return new ErrorResponse(errorCode, detail);
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, null);
    }
}