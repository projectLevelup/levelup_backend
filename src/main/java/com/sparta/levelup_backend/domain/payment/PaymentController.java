package com.sparta.levelup_backend.domain.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/payments")
public class PaymentController {

    @Value("${toss.client.key}")
    private String tossClientKey;

    @GetMapping("/client-key")
    public String getClientKey() {
        return tossClientKey;
    }
}
