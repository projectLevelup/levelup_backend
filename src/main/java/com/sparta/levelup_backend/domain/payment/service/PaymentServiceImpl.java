package com.sparta.levelup_backend.domain.payment.service;

import com.sparta.levelup_backend.domain.order.entity.OrderEntity;
import com.sparta.levelup_backend.domain.payment.dto.request.PaymentRequestDto;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import com.sparta.levelup_backend.domain.payment.repository.PaymentRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.BindException;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Value("${toss.client.key}")
    private String clientKey;

    @Value("${toss.secret.key}")
    private String secretKey;

    @Value("${toss.success.Url}")
    private String successUrl;

    @Value("${toss.fail.Url}")
    private String failUrl;

    @Override
    public PaymentResponseDto requestPayments(String customerId, PaymentRequestDto paymentRequestDto) {
        Long amount = paymentRequestDto.getAmount();
//        String payType = paymentRequestDto.getPayType().name();
        String customerEmail = paymentRequestDto.getCustomerEmail();
        String orderName = paymentRequestDto.getOrder().getProduct().getProductName();

        OrderEntity order = paymentRequestDto.getOrder();

        if (!amount.equals(order.getTotalPrice()))
            throw new BusinessException(ErrorCode.PAYMENT_ERROR_ORDER_PRICE);

        if (!payType.equals("CARD") && !payType.equals("TRANSFER")) {
            throw new BusinessException(ErrorCode.PAYMENT_ERROR_ORDER_NAME);
        }

        PaymentResponseDto paymentResponseDto;

        try {
            PaymentEntity payment = paymentRequestDto.toEntity();
            userRepository.findByCustomerKey(customerId)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
            paymentRepository.save(payment);

            paymentResponseDto = payment.toDto();
            paymentResponseDto.setSuccessUrl(successUrl);
            paymentResponseDto.setFailUrl(failUrl);
            return paymentResponseDto;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DB_ERROR_SAVE);
        }
    }
}
