package com.sparta.levelup_backend.exception.user;


import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;

public class ForbiddenException extends BusinessException {
  public ForbiddenException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ForbiddenException(ErrorCode errorCode, String detail){
    super(errorCode, detail);
  }

}
