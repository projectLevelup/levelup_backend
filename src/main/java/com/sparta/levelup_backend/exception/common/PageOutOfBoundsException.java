package com.sparta.levelup_backend.exception.common;

public class PageOutOfBoundsException extends BusinessException {
	public PageOutOfBoundsException(ErrorCode errorCode) {
		super(errorCode);
	}
}
