package com.sparta.levelup_backend.domain.payment.controller;
import com.sparta.levelup_backend.domain.payment.service.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.ResponseEntity.*;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentServiceImpl;

    @Value("${toss.secret.key}")
    private String tossSecretKey;

    /**
     * 결제 승인 요청 및 payments 생성
     * @throws Exception
     */
    @RequestMapping(value = {"/confirm/payment"})
    public ResponseEntity<JSONObject> confirmPayment(HttpServletRequest request, @RequestBody String jasonBody) throws Exception {
        JSONObject response = paymentServiceImpl.confirmPayment(jasonBody);
        int status = response.containsKey("error") ? 400 : 200;
        return status(status).body(response);
    }


}
