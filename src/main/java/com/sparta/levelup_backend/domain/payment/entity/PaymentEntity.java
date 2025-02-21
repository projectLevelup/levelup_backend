package com.sparta.levelup_backend.domain.payment.entity;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Setter
    @Column(name = "pay_type")
    private String payType;

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

    @Column(name = "customer_k")
    private String userKey;

    @Setter
    @Column(name = "payment_k")
    private String paymentKey;

    @Setter
    @Column(name = "completed_at")
    private String completedAt;

    @Setter
    @Column(name = "is_paid")
    private boolean ispaid;

    @Setter
    @Column(name = "is_canceled")
    private boolean iscanceled;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_name")
    private String customerName;
}
