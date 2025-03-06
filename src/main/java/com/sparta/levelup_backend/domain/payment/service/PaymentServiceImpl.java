package com.sparta.levelup_backend.domain.payment.service;

import com.sparta.levelup_backend.config.tossPayment.PaymentHttpClient;
import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.domain.bill.repository.BillRepository;
import com.sparta.levelup_backend.domain.bill.service.BillEventPubService;
import com.sparta.levelup_backend.domain.bill.service.BillServiceImplV2;
import com.sparta.levelup_backend.domain.payment.dto.request.CancelPaymentRequestDto;
import com.sparta.levelup_backend.domain.payment.entity.PaymentEntity;
import com.sparta.levelup_backend.domain.payment.repository.PaymentRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.service.ProductServiceImpl;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.exception.payment.PaymentException;
import com.sparta.levelup_backend.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;
import static com.sparta.levelup_backend.exception.common.ErrorCode.PAYMENT_FAILED;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RedisTemplate<String, String> redisTemplate;
    private final PaymentRepository paymentRepository;
    private final BillServiceImplV2 billService;
    private final BillRepository billRepository;
    private final RedissonClient redissonClient;
    private final ProductServiceImpl productService;
    private final BillEventPubService billEventPubService;
    private final PaymentHttpClient paymentHttpClient;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2000;

    /**
     * 결제 승인 요청 및 payments 생성
     * @throws Exception
     */
    @Override
    public JSONObject confirmPayment(String jsonBody) throws Exception {

        JSONObject jsonData = parseRequestData(jsonBody);
        String paymentKey = (String) jsonData.get("paymentKey");
        String price = (String) jsonData.get("amount");
        String orderId = (String) jsonData.get("orderId");

        PaymentEntity payment = validatePayment(orderId, price);

        String secretKey = paymentHttpClient.getTossSecretKey();
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        int attempt = 0;
        JSONObject response = null;

        while (attempt < MAX_RETRIES) {
            try {
                response = paymentHttpClient.sendRequest(jsonData,secretKey, url);
                int statusCode = response.containsKey("error") ? 400 : 200;

                if (statusCode == 200) {
                    updatePaymentInfo(payment, response, paymentKey, orderId);
                }
                return response;
            } catch (Exception e) {
                attempt++;
                log.error("결제 승인 요청 실패 - 시도 횟수: {}/{}, 내용: {}", attempt, MAX_RETRIES, e.getMessage());
                if (attempt >= MAX_RETRIES) {
                    throw new PaymentException(PAYMENT_FAILED_RETRY);
                }
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
        throw new PaymentException(PAYMENT_FAILED);
    }

    /**
     * 결제 취소 승인 요청
     * @param dto 프론트에서 결제취소정보 API 호출
     * @return 취소 완료
     * @throws Exception
     */
    public JSONObject cancelPayment(CancelPaymentRequestDto dto) throws Exception {

        PaymentEntity payment = paymentRepository.findByPaymentKey(dto.getKey())
                .orElseThrow(() -> new PaymentException(PAYMENT_NOT_FOUND));

        String secretKey = paymentHttpClient.getTossSecretKey();

        String url = "https://api.tosspayments.com/v1/payments/" + dto.getKey() + "/cancel";
        JSONObject cancelRequest = new JSONObject();
        cancelRequest.put("cancelReason", dto.getReason());

        int attempt = 0;
        JSONObject response = null;

        while (attempt < MAX_RETRIES) {
            try {
                response = paymentHttpClient.sendRequest(cancelRequest,secretKey, url);
                if (!response.containsKey("error")) {
                    handleCancelPayment(payment);
                    payment.setCanceled(true);
                    payment.getOrder().setStatus(OrderStatus.CANCELED);
                    paymentRepository.save(payment);

                    return response;
                }
            } catch (Exception e) {
                attempt++;
                log.error("취소 승인 요청 실패 - 시도 횟수: {}/{}, 내용: {}", attempt, MAX_RETRIES, e.getMessage());
                if (attempt >= MAX_RETRIES) {
                    throw new PaymentException(PAYMENT_FAILED_RETRY);
                }
                Thread.sleep(RETRY_DELAY_MS);
            }
        }
        throw new PaymentException(PAYMENT_FAILED);
    }

    private void handleCancelPayment(PaymentEntity payment) {

        BillEntity bill = billRepository.findByOrder(payment.getOrder())
                .orElseThrow(() -> new NotFoundException(BILL_NOT_FOUND));

        RLock lock = redissonClient.getLock("stock_lock_" + payment.getOrder().getProduct().getId());

        try {
            boolean available = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (!available) {
                throw new PaymentException(CONFLICT_LOCK_GET);
            }
            ProductEntity product = productService.getFindByIdWithLock(bill.getOrder().getProduct().getId());
            product.increaseAmount();
            billEventPubService.createCancelEvent(bill);
            log.info("상품: {} 수량 복구 완료", product.getProductName());

        } catch (InterruptedException e) {
            throw new PaymentException(CONFLICT_LOCK_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private PaymentEntity validatePayment(String orderId, String price) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(PAYMENT_NOT_FOUND));

        if (Long.parseLong(price) != payment.getAmount()) {
            throw new PaymentException(CONFLICT_PRICE_EQUALS);
        }

        return payment;
    }

    private JSONObject parseRequestData(String jsonBody) {
        try {
            return (JSONObject) new JSONParser().parse(jsonBody);
        } catch (ParseException e) {
            log.error("JSON Parsing Error", e);
            return new JSONObject();
        }
    }

    private void updatePaymentInfo(PaymentEntity payment, JSONObject response, String paymentKey, String orderId) {
        // 결제 승인 정보 추출
        String approvedAt = (String) response.get("approvedAt");
        String method = (String) response.get("method");
        String status = (String) response.get("status");

        log.info("결제 승인 완료 - paymentKey: {}, 승인시간: {}, 결제방법: {}, 상태: {}, orderId: {}", paymentKey, approvedAt, method, status, orderId);
        // 결제 정보 업데이트
        payment.setPaymentKey(paymentKey);
        payment.setPaid(true);
        payment.setCompletedAt(approvedAt);
        payment.setPayType(method);
        payment.getOrder().setStatus(OrderStatus.TRADING);
        billService.createBill(payment.getOrder().getUser().getId(), payment.getOrder().getId());
        redisTemplate.delete("order:expire:" + orderId);
        log.info("영수증 생성");
        paymentRepository.save(payment);
    }
}
