package com.sparta.levelup_backend.exception.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final ErrorCode errorCode;

  // 기본 생성자 - errorCode만을 받아 기본 메시지 사용
  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage()); // 상위 클래스의 message로 ErrorCode의 기본 메시지 사용
    this.errorCode = errorCode;
  }

  // 상세 메시지를 포함하는 생성자
  public BusinessException(ErrorCode errorCode, String detail) {
    super(detail); // 상위 클래스의 message로 상세 메시지 사용
    this.errorCode = errorCode;
  }
}
