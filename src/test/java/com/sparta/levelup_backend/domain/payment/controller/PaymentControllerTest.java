package com.sparta.levelup_backend.domain.payment.controller;

import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import com.sparta.levelup_backend.domain.payment.repository.PaymentRepository;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.PaymentException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;



@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class PaymentControllerTest {

    @Mock
    private PaymentRepository paymentRepository;

    private static final String ORDER_ID = "test-order-id";
    private static final String PAYMENT_KEY = "test-payment-Key";
    private static final long AMOUNT = 5000;

    private int attempt = 0;

    private void fakeHttpRequest() throws Exception {
        if (attempt < 2) {
            attempt++;
            throw new RuntimeException("Temporary Failure");
        }
    }

    @Test
    public void 재시도_성공_테스트() throws Exception {
        // Given
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .orderId(ORDER_ID)
                .amount(AMOUNT)
                .build();

        when(paymentRepository.findByOrderId(ORDER_ID)).thenReturn(Optional.of(paymentEntity));


        JSONObject requestJson = new JSONObject();
        requestJson.put("paymentKey", PAYMENT_KEY);
        requestJson.put("amount", String.valueOf(AMOUNT));
        requestJson.put("orderId", ORDER_ID);

       // when
        int maxRetries = 3;
        int retryCount = 0;
        boolean success = false;

        while (retryCount < maxRetries) {
            try {
                fakeHttpRequest();
                success = true;
                break;
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new PaymentException(ErrorCode.PAYMENT_FAILED_RETRY);
                }
            }
        }
        // Then
        assertTrue(success);
        assertEquals(2, attempt);
    }
}