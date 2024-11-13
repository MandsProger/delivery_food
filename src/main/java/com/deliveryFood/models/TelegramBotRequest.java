package com.deliveryFood.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "telegram_bot_requests", schema = "food")
@Getter
@Setter
public class TelegramBotRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status")
    private String status; // Например, "NEW", "IN_PROGRESS", "COMPLETED"

    @PrePersist
    private void init() {
        this.createdAt = LocalDateTime.now();
        this.status = "NEW"; // Статус нового обращения по умолчанию
    }
}