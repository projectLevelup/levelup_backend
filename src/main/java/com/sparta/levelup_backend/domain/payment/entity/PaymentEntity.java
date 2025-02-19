package com.sparta.levelup_backend.domain.payment.entity;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.utill.PayType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;

    @Column(nullable = false)
    private PayType payType;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "order_uuid",nullable = false)
    private String orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private LocalDateTime createDate;

    private String paySuccessYn;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private UserEntity customer;

    public PaymentResponseDto toDto() {
        return PaymentResponseDto.builder()
                .payType(payType.name())
                .amount(amount)
                .orderName(order.getProduct().getProductName())
                .customerEmail(customerEmail)
                .customerName(customerName)
                .createDate(createDate)
                .build();
    }
}
