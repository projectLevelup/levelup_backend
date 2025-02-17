package com.sparta.levelup_backend.common;


public class ApiResMessage {

    // 회원 관련 성공 메시지
    public static final String SIGNUP_SUCCESS = "회원가입이 성공적으로 완료되었습니다.";
    public static final String LOGIN_SUCCESS = "로그인이 성공적으로 완료되었습니다.";
    public static final String FIND_SUCCESS = "사용자 정보 조회에 성공했습니다.";
    public static final String UPDATE_SUCCESS = "사용자 정보 수정에 성공했습니다.";
    public static final String PASSWORD_CHANGE_SUCCESS = "사용자 비밀번호 변경에 성공했습니다.";
    public static final String DELETE_SUCCESS = "탈퇴가 완료되었습니다.";


    //Review
    public static final String REVIEW_SUCCESS = "리뷰 작성이 완료되었습니다.";
    public static final String REVIEW_DELETE = "리뷰 삭제가 완료되었습니다.";
    public static final String REVIEW_LIST_SUCCESS = "리뷰 목록 조회가 완료되었습니다.";

    // Order
    public static final String ORDER_CREATE = "주문이 생성되었습니다";
    public static final String ORDER_FIND = "주문이 조회되었습니다.";
    public static final String ORDER_UPDATE = "주문 상태가 변경되었습니다.";
    public static final String ORDER_COMPLETE = "거래가 완료되었습니다.";
    public static final String ORDER_CANCLED = "주문 및 거래가 취소되었습니다.";

    // Product
    public static final String PRODUCT_CREATE = "상품이 생성되었습니다";
    public static final String PRODUCT_READ = "상품이 조회되었습니다";
    public static final String PRODUCT_UPDATE = "상품이 수정되었습니다";
    public static final String PRODUCT_DELETE = "상품이 삭제되었습니다";

    // 게임 관련 성공 메시지
    public static final String GAME_SAVE_SUCCESS = "게임 등록이 성공적으로 완료되었습니다.";
    public static final String GAME_FOUND_SUCCESS = "게임을 성공적으로 조회되었습니다.";
    public static final String GAME_UPDATE_SUCCESS = "게임을 성공적으로 수정되었습니다.";
    public static final String GAME_DELETE_SUCCESS = "게임이 성공적으로 삭제되었습니다.";

    // Bill
    public static final String BILL_FIND = "결제 내역이 조회되었습니다.";
    public static final String BILL_DELETE = "결제 내역이 삭제되었습니다.";

    // Community
    public static final String COMMUNITY_SAVE_SUCCESS = "커뮤니티 글이 생성되었습니다.";
    public static final String COMMUNITY_LIST_FOUND_SUCCESS = "커뮤니티 글 목록이 조회되었습니다.";
    public static final String COMMUNITY_UPDATE_SUCCESS = "커뮤니티 글이 수정되었습니다.";

}

