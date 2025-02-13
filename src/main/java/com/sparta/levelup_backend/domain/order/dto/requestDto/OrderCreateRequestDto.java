package com.sparta.levelup_backend.domain.order.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.sparta.levelup_backend.domain.order.dto.OrderValidationMessage.*;

@Getter
@RequiredArgsConstructor
public class OrderCreateRequestDto {

    @NotNull(message = PRODUCT_BLANK_MESSAGE)
    private final Long productId;
}
