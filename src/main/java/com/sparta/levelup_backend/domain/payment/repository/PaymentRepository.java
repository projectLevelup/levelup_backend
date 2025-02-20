package com.sparta.levelup_backend.domain.payment.repository;

import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByOrderId(String orderId);

    Optional<PaymentEntity> findByPaymentKeyAndCustomer_Email(String paymentKey, String email);

    Slice<PaymentEntity> findAllByCustomer_Email(String email, Pageable pageable);
}
