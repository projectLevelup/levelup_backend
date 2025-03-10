package com.sparta.levelup_backend.exception.common;

import com.sparta.levelup_backend.enums.ErrorCode;

public class BadRequestException extends BusinessException {
	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
