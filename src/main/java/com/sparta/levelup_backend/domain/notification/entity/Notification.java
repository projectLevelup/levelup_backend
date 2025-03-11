package com.sparta.levelup_backend.domain.notification.entity;

import com.sparta.levelup_backend.enums.NotificationStatus;
import com.sparta.levelup_backend.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.sparta.levelup_backend.enums.NotificationStatus.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static java.time.LocalDateTime.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long userId;

    private String message;

    @Enumerated(STRING)
    private NotificationType type;

    @Setter
    @Enumerated(STRING)
    private NotificationStatus status;

    private LocalDateTime createdAt;

    public static Notification create(Long userId, String message, NotificationType type) {
        return Notification.builder()
                .userId(userId)
                .message(message)
                .type(type)
                .status(UNREAD)
                .createdAt(now())
                .build();
    }
}
