package com.example.ordertracker.mapper;

import com.example.ordertracker.entity.WebhookEventLog;
import com.example.ordertracker.model.response.WebhookEventLogResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebhookLogMapper {

    WebhookEventLogResponse toResponse(WebhookEventLog webhookEventLog);
}
