package com.sparta.levelup_backend.domain.payment.controller;

import com.sparta.levelup_backend.common.apiresponse.ApiResponse;
import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.payment.dto.request.CancelPaymentRequestDto;
import com.sparta.levelup_backend.domain.payment.dto.response.CancelResponseDto;
import com.sparta.levelup_backend.domain.payment.dto.response.PaymentResponseDto;
import com.sparta.levelup_backend.domain.payment.service.PaymentRestService;
import com.sparta.levelup_backend.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentRestService paymentRestService;
    private final PaymentService paymentService;

    @PostMapping("/request/{orderId}")
    public ApiResponse<PaymentResponseDto> createPayment(
            @AuthenticationPrincipal CustomUserDetails auth,
            @PathVariable Long orderId
    ) {
        return success(OK, OK_REQUEST, paymentRestService.createPayment(auth, orderId));
    }

    /**
     * 결제 취소 승인 요청
     * @param dto 프론트에서 결제취소정보 API 호출
     * @return 취소 완료
     * @throws Exception
     */
    @RequestMapping("/cancel/payment")
    public ApiResponse<JSONObject> cancelPayment(@RequestBody CancelPaymentRequestDto dto) throws Exception {
        JSONObject response = paymentService.cancelPayment(dto);
        return success(OK, OK_CANCEL, response);
    }

    @PostMapping("/request/cancel")
    public ApiResponse<CancelResponseDto> requestCancel(
            @AuthenticationPrincipal CustomUserDetails auth,
            @RequestBody CancelPaymentRequestDto dto
    ) {
        return success(OK, OK_REQUEST_CANCEL, paymentRestService.requestCancel(auth, dto));
    }
}
