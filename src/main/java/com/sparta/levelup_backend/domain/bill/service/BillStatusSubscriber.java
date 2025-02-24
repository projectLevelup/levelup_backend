package com.sparta.levelup_backend.domain.bill.service;

import com.google.gson.Gson;
import com.sparta.levelup_backend.domain.bill.dto.responseDto.BillStatusMessageDto;
import com.sparta.levelup_backend.utill.BillStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillStatusSubscriber implements MessageListener {
    private final NotificationService notificationService;
    private final Gson gson = new Gson();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String jsonMessage = new String(message.getBody());
        BillStatusMessageDto billMessage = BillStatusMessageDto.fromJson(jsonMessage);

        if (billMessage.getStatus() == BillStatus.PAID) {
            // 결제 완료 -> 판매자에게 알림
            notificationService.notifyTutor(billMessage);
        }

        if (billMessage.getStatus() == BillStatus.PAYCANCELED) {
            // 취소 승인 -> 구매자에게 알림
            notificationService.notifyStudent(billMessage);
        }
        log.info("상태 변환 이벤트 발생: {}", billMessage);
    }
}
