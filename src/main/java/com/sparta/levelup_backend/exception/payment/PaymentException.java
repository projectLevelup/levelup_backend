package com.sparta.levelup_backend.exception.payment;

import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.enums.ErrorCode;

public class PaymentException extends BusinessException {
  public PaymentException(ErrorCode errorCode) {
    super(errorCode);
  }

  public PaymentException(ErrorCode errorCode, String detail) {
    super(errorCode, detail);
  }
}


