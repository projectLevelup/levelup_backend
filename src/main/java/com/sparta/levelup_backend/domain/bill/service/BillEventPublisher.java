package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.dto.PubBillDto;
import com.sparta.levelup_backend.domain.bill.entity.BillEntity;
import com.sparta.levelup_backend.enums.BillStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.sparta.levelup_backend.enums.BillStatus.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class BillEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    // bill 상태가 변경될 때 메세지 전송
    public void publishBillStatusChange(BillEntity bill) {
        String routingKey = getRoutingKey(bill.getStatus());

        PubBillDto billDto = new PubBillDto();
        billDto.setBillId(bill.getId());
        billDto.setStatus(bill.getStatus());
        billDto.setTutorId(bill.getTutor().getId());
        billDto.setStudentId(bill.getStudent().getId());

        log.info("메세지 전송: billId= {}, 상태= {}", bill.getId(), bill.getStatus());
        rabbitTemplate.convertAndSend("bill.exchange", routingKey, billDto);
    }

    // 상태에 따른 라우팅 키 설정
    private String getRoutingKey(BillStatus status) {
        if (status == PAID) {
            return "bill.paid";
        }
        if (status == PAYCANCELED) {
            return "bill.paycanceled";
        }
        return null;
    }
}
