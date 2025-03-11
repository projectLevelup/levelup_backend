package com.sparta.levelup_backend.exception.game;

import com.sparta.levelup_backend.enums.ErrorCode;
import com.sparta.levelup_backend.exception.common.BusinessException;

public class GameException extends BusinessException {
	public GameException(ErrorCode errorCode) { super(errorCode);}
}
