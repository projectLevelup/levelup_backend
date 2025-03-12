package com.sparta.levelup_backend.exception.chat;

import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.common.BusinessException;

public class ChatException extends BusinessException {
	public ChatException(ErrorCode errorCode) {
		super(errorCode);
	}
}
