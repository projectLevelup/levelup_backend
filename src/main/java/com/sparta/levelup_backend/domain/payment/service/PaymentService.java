package com.sparta.levelup_backend.domain.payment.service;

import com.sparta.levelup_backend.domain.payment.dto.request.CancelPaymentRequestDto;
import org.json.simple.JSONObject;

public interface PaymentService {

    JSONObject confirmPayment(String jasonBody) throws Exception;
    JSONObject cancelPayment(CancelPaymentRequestDto dto) throws Exception;
}
