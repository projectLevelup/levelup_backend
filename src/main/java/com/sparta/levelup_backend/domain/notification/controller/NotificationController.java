package com.sparta.levelup_backend.domain.notification.controller;

import com.sparta.levelup_backend.common.apiresponse.ApiResponse;
import com.sparta.levelup_backend.domain.notification.entity.Notification;
import com.sparta.levelup_backend.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 특정 사용자의 알림 가져오기
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ApiResponse<List<Notification>> getNotifications(@PathVariable Long userId) {
        return success(OK, NOTIFICATION_LIST_FOUND_SUCCESS, notificationService.getNotifications(userId));
    }

    /**
     * 알림 읽음 처리 기능
     * @param notificationId
     * @return
     */
    @PostMapping("/{notificationId}/read")
    public ApiResponse<Void> checkRead(@PathVariable Long notificationId) {
        notificationService.checkRead(notificationId);
        return success(OK, NOTIFICATION_CHECK_SUCCESS);
    }

    /**
     * 결제 완료 시 알림 전송
     */
    public void sendPaymentSuccessNotification(Long userId, String message) {
        String destination = "/sub/notification/user/" + userId;
        messagingTemplate.convertAndSend(destination, message);
        log.info("결제 완료 알림 전송: {}", message);
    }

    /**
     * 결제 취소 시 알림 전송
     */
    public void sendPaymentCanceledNotification(Long userId, String message) {
        String destination = "/sub/notification/user/" + userId;
        messagingTemplate.convertAndSend(destination, message);
        log.info("결제 취소 알림 전송: {}", message);
    }
}
