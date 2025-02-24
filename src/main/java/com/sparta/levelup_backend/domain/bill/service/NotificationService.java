package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.dto.responseDto.BillStatusMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    public void notifyTutor(BillStatusMessageDto billMessage) {
      log.info("판매자({})에게 상품: {} 결제 완료 알림을 전송했습니다.", billMessage.getTutorId(), billMessage.getBillHistory());
    }

    public void notifyStudent(BillStatusMessageDto billMessage) {
        log.info("구매자({})에게 상품: {} 결제 취소 알림을 전송했습니다.", billMessage.getStudentId(), billMessage.getBillHistory());
    }
}
