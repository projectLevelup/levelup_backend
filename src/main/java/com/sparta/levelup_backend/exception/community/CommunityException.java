package com.sparta.levelup_backend.exception.community;

import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.common.BusinessException;

public class CommunityException extends BusinessException {
	public CommunityException(ErrorCode errorCode) { super(errorCode);}
}
