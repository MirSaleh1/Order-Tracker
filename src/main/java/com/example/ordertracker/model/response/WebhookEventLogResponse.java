package com.example.ordertracker.model.response;

import com.example.ordertracker.model.WebhookStatus;
import java.time.LocalDateTime;

public record WebhookEventLogResponse(
        Long id,
        String provider,
        String eventType,
        WebhookStatus status,
        LocalDateTime receivedAt,
        String errorMessage,
        String payload
) {}