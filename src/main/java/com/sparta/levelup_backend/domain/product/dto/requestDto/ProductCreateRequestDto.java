package com.sparta.levelup_backend.domain.product.dto.requestDto;

import com.sparta.levelup_backend.domain.product.dto.ValidMessage;
import com.sparta.levelup_backend.utill.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {

	@NotNull(message = ValidMessage.USER_ID_REQUIRED)
	private Long userId;

	@NotNull(message = ValidMessage.GAME_ID_REQUIRED)
	private Long gameId;

	@NotNull(message = ValidMessage.PRODUCT_NAME_REQUIRED)
	@Size(max = 255, message = ValidMessage.PRODUCT_NAME_LENGTH)
	private String productName;

	@NotNull(message = ValidMessage.CONTENTS_REQUIRED)
	@Size(max = 1000, message = ValidMessage.CONTENTS_LENGTH)
	private String contents;

	@NotNull(message = ValidMessage.PRICE_REQUIRED)
	private Long price;

	@NotNull(message = ValidMessage.AMOUNT_REQUIRED)
	private Integer amount;

	@NotNull(message = ValidMessage.STATUS_REQUIRED)
	private ProductStatus status;

	private String imgUrl; // ✅ 이미지 URL은 선택 입력값이므로 `@NotNull` 제거
}
