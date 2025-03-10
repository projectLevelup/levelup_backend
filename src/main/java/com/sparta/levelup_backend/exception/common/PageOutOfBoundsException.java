package com.sparta.levelup_backend.exception.common;

import com.sparta.levelup_backend.enums.ErrorCode;

public class PageOutOfBoundsException extends BusinessException {
	public PageOutOfBoundsException(ErrorCode errorCode) {
		super(errorCode);
	}
}
