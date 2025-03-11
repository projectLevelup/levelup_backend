package com.sparta.levelup_backend.exception.product;

import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.enums.ErrorCode;

public class ProductException extends BusinessException {
  public ProductException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ProductException(ErrorCode errorCode, String detail) {
    super(errorCode, detail);
  }

}


