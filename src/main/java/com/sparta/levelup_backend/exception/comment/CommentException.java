package com.sparta.levelup_backend.exception.comment;

import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.common.BusinessException;

public class CommentException extends BusinessException {
	public CommentException(ErrorCode errorCode) { super(errorCode);}
}
