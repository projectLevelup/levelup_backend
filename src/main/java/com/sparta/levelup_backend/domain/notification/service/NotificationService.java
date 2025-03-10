package com.sparta.levelup_backend.domain.notification.service;

import com.sparta.levelup_backend.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getNotifications(Long userId);

    void checkRead(Long notificationId);
}
