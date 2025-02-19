package com.sparta.levelup_backend.domain.payment.dto.request;

import com.sparta.levelup_backend.common.entity.BaseEntity;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import com.sparta.levelup_backend.utill.PayType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

import static com.sparta.levelup_backend.domain.payment.dto.PaymentValidationMessage.*;

@Getter
@Builder
@AllArgsConstructor
public class PaymentRequestDto {

    private PayType payType;

    private Long amount;

    private OrderEntity order ;

    private String customerEmail;

    private String customerName;

    public PaymentEntity toEntity() {
        return PaymentEntity.builder()
                .orderId(UUID.randomUUID().toString())
                .payType(payType)
                .amount(amount)
                .order(order)
                .customerEmail(customerEmail)
                .customerName(customerName)
                .paySuccessYn("Y")
                .createDate(new BaseEntity().getCreatedAt())
                .build();

    }
}
