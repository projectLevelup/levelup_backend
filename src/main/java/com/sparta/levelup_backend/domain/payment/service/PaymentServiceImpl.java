package com.sparta.levelup_backend.domain.payment.service;

import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.config.TossPaymentConfig;
import com.sparta.levelup_backend.domain.bill.service.BillServiceImplV2;
import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.order.repository.OrderRepository;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import com.sparta.levelup_backend.domain.payment.repository.PaymentRepository;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.*;
import com.sparta.levelup_backend.utill.OrderStatus;
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
    private final BillServiceImplV2 billService;

    @Transactional
    @Override
    public PaymentResponseDto createPayment(CustomUserDetails auth, Long orderId) {
        Long userId = auth.getId();
        OrderEntity order = orderRepository.findByIdOrElseThrow(orderId);

        if (!order.getUser().getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_ACCESS);
        }

        log.info("주문상태: {}", order.getStatus());
        // 결제 대기 상태가 아니라면 변경 불가
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderException(ErrorCode.INVALID_ORDER_STATUS);
        }


        PaymentEntity existingPayment = paymentRepository.findByOrder(order);
        if (existingPayment != null) {
            PaymentResponseDto response = new PaymentResponseDto(existingPayment);
            response.setSuccessUrl(tossPaymentConfig.getSuccessUrl());
            response.setFailUrl(tossPaymentConfig.getFailUrl());
            log.info("결제정보Id: {}", existingPayment.getPaymentId());
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
        log.info("결제정보Id 생성: {}", payment.getPaymentId());

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
        payment.getOrder().setStatus(OrderStatus.TRADING);
        billService.createBill(payment.getOrder().getUser().getId(), payment.getOrder().getId());
        log.info("영수증 생성");

        paymentRepository.save(payment);
    }
}
