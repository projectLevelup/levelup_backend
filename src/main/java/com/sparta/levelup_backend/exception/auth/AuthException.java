package com.sparta.levelup_backend.exception.auth;

import com.sparta.levelup_backend.enums.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class AuthException extends AuthenticationException {

    private final OAuth2Error error;
    public AuthException(ErrorCode errorCode) {
        super(errorCode.toString());
        error = new OAuth2Error(errorCode.toString());
    }

    public OAuth2Error getError() {
        return this.error;
    }
}
