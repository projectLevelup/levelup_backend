package com.sparta.levelup_backend.domain.payment.controller;

import com.sparta.levelup_backend.domain.bill.service.BillServiceImplV2;
import com.sparta.levelup_backend.domain.payment.dto.request.CancelPaymentRequestDto;
import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import com.sparta.levelup_backend.domain.payment.repository.PaymentRepository;
import com.sparta.levelup_backend.domain.payment.service.PaymentService;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.exception.common.PaymentException;
import com.sparta.levelup_backend.utill.OrderStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final BillServiceImplV2 billService;

    @Value("${toss.secret.key}")
    private String tossSecretKey;

    @RequestMapping(value = {"/confirm/payment"})
    public ResponseEntity<JSONObject> confirmPayment(HttpServletRequest request, @RequestBody String jasonBody) throws Exception {

        JSONObject jasonData = parseRequestData(jasonBody);
        String paymentKey = (String) jasonData.get("paymentKey");
        String price = (String) jasonData.get("amount");
        String orderId = (String) jasonData.get("orderId");

        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND));

        if (Long.parseLong(price) != payment.getAmount()) {
            throw new PaymentException(ErrorCode.CONFLICT_PRICE_EQUALS);
        }

        String secretKey = tossSecretKey;
        JSONObject response = sendRequest(parseRequestData(jasonBody), secretKey, "https://api.tosspayments.com/v1/payments/confirm");
        int statusCode = response.containsKey("error") ? 400 : 200;

        if (statusCode == 200) {
            // 결제 승인 정보 추출
            String approvedAt = (String) response.get("approvedAt");
            String method = (String) response.get("method");
            String status = (String) response.get("status");

            log.info("paymentKey: {}, 승인시간: {}, 결제방법: {}, 상태: {}, orderId: {}", paymentKey, approvedAt, method, status, orderId);
            // 결제 정보 업데이트
            payment.setPaymentKey(paymentKey);
            payment.setIspaid(true);
            payment.setCompletedAt(approvedAt);
            payment.setPayType(method);
            payment.getOrder().setStatus(OrderStatus.TRADING);
            billService.createBill(payment.getOrder().getUser().getId(), payment.getOrder().getId());
            log.info("영수증 생성");
            paymentRepository.save(payment);
        }

        return ResponseEntity.status(statusCode).body(response);
    }

    @RequestMapping("/cancel/payment")
    public ResponseEntity<JSONObject> cancelPayment(@RequestBody CancelPaymentRequestDto dto) {

        PaymentEntity payment = paymentRepository.findByPaymentKey(dto.getKey())
                .orElseThrow(() -> new PaymentException(ErrorCode.PAYMENT_NOT_FOUND));


    }


    private JSONObject parseRequestData(String jsonBody) {
        try {
            return (JSONObject) new JSONParser().parse(jsonBody);
        } catch (ParseException e) {
            logger.error("JSON Parsing Error", e);
            return new JSONObject();
        }
    }

    // 헤더에 시크릿키를 심어서 같이 요청
    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            logger.error("Error reading response", e);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Error reading response");
            return errorResponse;
        }
    }

    // 헤더에 시크릿키 담기
    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }
}
