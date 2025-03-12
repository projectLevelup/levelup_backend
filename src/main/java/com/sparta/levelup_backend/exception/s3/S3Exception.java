package com.sparta.levelup_backend.exception.s3;

import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.common.BusinessException;

public class S3Exception extends BusinessException {
	public S3Exception(ErrorCode errorCode) { super(errorCode);}
}