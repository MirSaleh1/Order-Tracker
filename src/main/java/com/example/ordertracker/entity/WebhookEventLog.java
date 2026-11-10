package com.example.ordertracker.entity;

import com.example.ordertracker.model.WebhookStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_event_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String provider;
    @Column(nullable = false)
    private String eventType;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WebhookStatus status;
    @Column(nullable = false)
    private LocalDateTime receivedAt;
    private String errorMessage;
}
