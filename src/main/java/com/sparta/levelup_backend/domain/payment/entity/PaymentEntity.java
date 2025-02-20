package com.sparta.levelup_backend.domain.payment.entity;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.utill.PayType;
import jakarta.persistence.*;
import lombok.*;
import net.bytebuddy.utility.nullability.MaybeNull;

import java.time.LocalDateTime;

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

    @Column(unique = true, name = "order_id")
    private String orderId;

    @Column(name = "pay_type")
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(nullable = false, name = "pay_amount")
    private Long amount;

    @Column(nullable = false, name = "pay_name")
    private String orderName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_order", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity customer;

    @Column(name = "payment_k")
    private String paymentKey;

    @Column(name = "completed_at")
    private String completedAt;

    @Column(name = "is_paid")
    private boolean ispaid;

    @Column(name = "is_canceled")
    private boolean iscanceled;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_name")
    private String customerName;
}
