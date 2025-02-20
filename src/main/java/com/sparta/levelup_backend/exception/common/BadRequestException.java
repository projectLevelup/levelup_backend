package com.sparta.levelup_backend.exception.common;

public class BadRequestException extends BusinessException {
	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
