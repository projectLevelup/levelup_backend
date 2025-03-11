package com.sparta.levelup_backend.exception.review;

import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.common.BusinessException;

public class ReviewException extends BusinessException {
	public ReviewException(ErrorCode errorCode) {
		super(errorCode);
	}
}