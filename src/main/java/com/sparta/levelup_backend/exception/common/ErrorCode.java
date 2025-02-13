package com.sparta.levelup_backend.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 BAD_REQUEST
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "ERR001", "요청값이 올바르지 않습니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "ERR002", "잘못된 JSON 형식입니다."),
    INVALID_FORMAT_TOKEN(HttpStatus.BAD_REQUEST, "ERR007", "지원되지 않는 JWT 토큰입니다."),
    INVALID_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "ERR008", "현재 비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "ERR009", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "ERR101", "로그인이 필요합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED,"ERR102","로그인에 실패했습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED,"ERR103", "유효하지 않는 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "ERR104", "만료된 JWT 토큰입니다."),

    // 403 FORBIDDEN
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "ERR201", "접근 권한이 없습니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR301", "사용자를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR302", "상품을 찾을 수 없습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"ERR303", "토큰을 찾을 수 없습니다."),
    // 409 CONFLICT
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "ERR401", "이미 사용 중인 이메일입니다."),
    DUPLICATE_OUT_OF_AMOUNT(HttpStatus.CONFLICT, "ERR402", "재고가 소진되었습니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERR501", "서버 내부 오류가 발생했습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
