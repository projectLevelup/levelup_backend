package com.sparta.levelup_backend.exception.bill;

import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.common.BusinessException;

public class BillException extends BusinessException {
    public BillException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BillException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
