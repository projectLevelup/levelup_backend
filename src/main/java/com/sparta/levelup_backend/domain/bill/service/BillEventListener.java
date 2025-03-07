package com.sparta.levelup_backend.domain.bill.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class BillEventListener {

    // 결제 완료 되었을때
    @RabbitListener(queues = "bill.paid.queue")
    public void handleBillPaid(Map<String, Object> message) {
        Long billId = (Long) message.get("billId");
        Long sellerId = (Long) message.get("tutorId");

        log.info("판매자({})에게 결제 완료 알림: billId= {}", sellerId, billId);

        // TODO: 이후 전송 방식
    }

    // 결제 취소 되었을때
    @RabbitListener(queues = "bill.paycanceled.queue")
    public void handleBillPayCanceled(Map<String, Object> message) {
        Long billId = (Long) message.get("billId");
        Long studentId = (Long) message.get("studentId");

        log.info("구매자({})에게 결제 취소 알림: billId= {}", studentId, billId);

        // TODO: 이후 전송 방식
    }
}
