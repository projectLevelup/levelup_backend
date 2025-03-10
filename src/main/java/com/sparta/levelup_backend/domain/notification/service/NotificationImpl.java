package com.sparta.levelup_backend.domain.notification.service;

import com.sparta.levelup_backend.domain.notification.entity.Notification;
import com.sparta.levelup_backend.domain.notification.repository.NotificationRepository;
import com.sparta.levelup_backend.enums.NotificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sparta.levelup_backend.enums.NotificationStatus.*;

@Service
@RequiredArgsConstructor
public class NotificationImpl implements NotificationService{

    private final NotificationRepository notificationRepository;

    /**
     * 특정 유저의 알림 가져오기
     * @param userId
     * @return
     */
    @Override
    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * 알림 읽음 처리
     * @param notificationId
     */
    @Override
    public void checkRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setStatus(READ);
            notificationRepository.save(notification);
        });
    }


}
