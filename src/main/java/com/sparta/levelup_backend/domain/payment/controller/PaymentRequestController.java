package com.sparta.levelup_backend.domain.payment.controller;

import com.sparta.levelup_backend.common.ApiResMessage;
import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.payment.dto.request.PaymentRequestDto;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.levelup_backend.domain.payment.service.PaymentServiceImpl;
import com.sparta.levelup_backend.exception.common.BusinessException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v3/payments")
@RequiredArgsConstructor
public class PaymentRequestController {

    private final PaymentServiceImpl paymentServiceImpl;

    @PostMapping("/request")
    public ApiResponse<PaymentResponseDto> requestPayments(
            @AuthenticationPrincipal CustomUserDetails auth,
            @ModelAttribute PaymentRequestDto paymentRequestDto
    ) {
        try {
            String customerKey = auth.getCustomerKey();
            PaymentResponseDto save = paymentServiceImpl.requestPayments(customerKey,paymentRequestDto);
            return success(OK, OK_REQUEST, save);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

    }
}
