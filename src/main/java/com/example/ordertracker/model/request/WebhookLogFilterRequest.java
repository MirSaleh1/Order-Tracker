package com.example.ordertracker.model.request;

import com.example.ordertracker.model.WebhookStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record WebhookLogFilterRequest(
        String provider,
        WebhookStatus status,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) {}