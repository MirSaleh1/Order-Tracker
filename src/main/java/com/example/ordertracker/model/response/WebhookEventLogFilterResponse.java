package com.example.ordertracker.model.response;

import java.util.List;

public record WebhookEventLogFilterResponse(
        List<WebhookEventLogResponse> logs,
        int totalPages,
        long totalElements,
        boolean hasNext
) {}