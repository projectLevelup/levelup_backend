package com.sparta.levelup_backend.domain.notification.entity;

import com.sparta.levelup_backend.enums.NotificationStatus;
import com.sparta.levelup_backend.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Setter
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime createdAt;

    public static Notification create(Long userId, String message, NotificationType type) {
        return Notification.builder()
                .userId(userId)
                .message(message)
                .type(type)
                .status(NotificationStatus.UNREAD)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
