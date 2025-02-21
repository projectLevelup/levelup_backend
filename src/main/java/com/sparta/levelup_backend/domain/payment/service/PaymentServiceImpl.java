package com.sparta.levelup_backend.domain.payment.service;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.config.TossPaymentConfig;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import com.sparta.levelup_backend.domain.payment.repository.PaymentRepository;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TossPaymentConfig tossPaymentConfig;

    @Transactional
    @Override
    public PaymentResponseDto createPayment(CustomUserDetails auth, Long orderId) {
        Long userId = auth.getId();
        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ACCESS);
        }

        PaymentEntity existingPayment = paymentRepository.findByOrder(order);
        if (existingPayment != null) {
            PaymentResponseDto response = new PaymentResponseDto(existingPayment);
            response.setSuccessUrl(tossPaymentConfig.getSuccessUrl());
            response.setFailUrl(tossPaymentConfig.getFailUrl());
            return response;
        }

        PaymentEntity payment = PaymentEntity.builder()
                .orderId(UUID.randomUUID().toString())
                .order(order)
                .customer(order.getUser())
                .orderName(order.getProduct().getProductName())
                .amount(order.getTotalPrice())
                .customerName(order.getUser().getNickName())
                .customerEmail(order.getUser().getEmail())
                .userKey(order.getUser().getCustomerKey())
                .ispaid(false)
                .iscanceled(false)
                .build();

        PaymentResponseDto response = new PaymentResponseDto(payment);
        response.setSuccessUrl(tossPaymentConfig.getSuccessUrl());
        response.setFailUrl(tossPaymentConfig.getFailUrl());
        paymentRepository.save(payment);

        return response;
    }

    @Override
    @Transactional
    public void updatePayment(String paymentKey, String approvedAt, String method, String orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND));

        payment.setPaymentKey(paymentKey);
        payment.setIspaid(true);
        payment.setCompletedAt(approvedAt);
        payment.setPayType(method);

        paymentRepository.save(payment);
    }
}
