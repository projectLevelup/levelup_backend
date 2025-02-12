package com.sparta.levelup_backend.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 BAD_REQUEST
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "ERR001", "요청값이 올바르지 않습니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "ERR002", "잘못된 JSON 형식입니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "ERR003", "결제 정책에 의해 상태 변경 요청이 불가능합니다."),
    INVALID_ORDER_CANCELED(HttpStatus.BAD_REQUEST, "ERR005", "주문 취소 기능 요청으로 가능합니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "ERR101", "로그인이 필요합니다."),

    // 403 FORBIDDEN
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "ERR201", "접근 권한이 없습니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR301", "사용자를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR302", "상품을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR304", "주문을 찾을 수 없습니다."),

    // 409 CONFLICT
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "ERR401", "이미 사용 중인 이메일입니다."),
    DUPLICATE_OUT_OF_AMOUNT(HttpStatus.CONFLICT, "ERR402", "재고가 소진되었습니다."),
    DUPLICATE_CANCELED_ORDER(HttpStatus.CONFLICT, "ERR403", "이미 주문취소가 되었습니다."),

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
