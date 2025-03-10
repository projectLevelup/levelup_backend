package com.sparta.levelup_backend.domain.bill.service;

import com.sparta.levelup_backend.domain.bill.dto.PubBillDto;
import com.sparta.levelup_backend.domain.notification.controller.NotificationController;
import com.sparta.levelup_backend.domain.notification.entity.Notification;
import com.sparta.levelup_backend.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


import static com.sparta.levelup_backend.enums.NotificationType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillEventListener {

    private final NotificationRepository notificationRepository;
    private final NotificationController notificationController;

    // 결제 완료 되었을때
    @RabbitListener(queues = "bill.paid.queue")
    public void handleBillPaid(@Payload PubBillDto dto) {
        Long billId = dto.getBillId();
        Long tutorId = dto.getTutorId();
        String notificationMessage = "결제 완료 : 주문번호 " + billId;

        log.info("판매자({})에게 결제 완료 알림: billId= {}", tutorId, billId);
        Notification notification = Notification.create(tutorId, notificationMessage, BILL_PAID);
        notificationRepository.save(notification);

        notificationController.sendPaymentSuccessNotification(tutorId, notificationMessage);
    }

    // 결제 취소 되었을때
    @RabbitListener(queues = "bill.paycanceled.queue")
    public void handleBillPayCanceled(PubBillDto dto) {
        Long billId = dto.getBillId();
        Long studentId = dto.getStudentId();
        String notificationMessage = "결제 취소 : 주문번호 " + billId;

        log.info("구매자({})에게 결제 취소 알림: billId= {}", studentId, billId);
        Notification notification = Notification.create(studentId, notificationMessage, BILL_CANCELED);
        notificationRepository.save(notification);

        notificationController.sendPaymentCanceledNotification(studentId, notificationMessage);
    }
}
