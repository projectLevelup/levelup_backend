package com.sparta.levelup_backend.domain.product.dto;

public class ProductValidMessage {
	public static final String PRODUCT_ID_REQUIRED = "상품 ID는 필수 입력 항목입니다.";
	public static final String STATUS_REQUIRED = "상품 상태는 필수 입력 항목입니다.";
	public static final String PRICE_REQUIRED = "가격은 필수 입력 항목입니다.";
	public static final String AMOUNT_REQUIRED = "수량은 필수 입력 항목입니다.";
	public static final String PRODUCT_NAME_REQUIRED = "상품명은 필수 입력 항목입니다.";
	public static final String CONTENTS_REQUIRED = "상품 설명은 필수 입력 항목입니다.";

	public static final String PRODUCT_NAME_LENGTH = "상품명은 최대 255자까지 입력 가능합니다.";
	public static final String CONTENTS_LENGTH = "상품 설명은 최대 1000자까지 입력 가능합니다.";

}
