package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.enums.BillStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    // bill 상태가 변경될 때 메세지 전송
    public void publishBillStatusChange(BillEntity bill) {
        String routingKey = getRoutingKey(bill.getStatus());
        if (routingKey != null) {
            Map<String, Object> message = new HashMap<>();
            message.put("billId", bill.getId());
            message.put("status", bill.getStatus());
            message.put("tutorId", bill.getTutor().getId());
            message.put("studentId", bill.getStudent().getId());

            log.info("메세지 전송: billId= {}, 상태= {}", bill.getId(), bill.getStatus());
            rabbitTemplate.convertAndSend("bill.exchange", routingKey, bill.getId());
        }
    }

    // 샅애에 따른 라우팅 키 설정
    private String getRoutingKey(BillStatus status) {
        if (status == BillStatus.PAID) {
            return "bill.paid";
        }
        if (status == BillStatus.PAYCANCELED) {
            return "bill.paycanceled";
        }
        return null;
    }
}
