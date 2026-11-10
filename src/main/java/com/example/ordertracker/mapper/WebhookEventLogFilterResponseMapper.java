package com.example.ordertracker.mapper;

import com.example.ordertracker.entity.WebhookEventLog;
import com.example.ordertracker.model.response.WebhookEventLogFilterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = WebhookLogMapper.class)
public interface WebhookEventLogFilterResponseMapper {

    @Mapping(target = "logs", source = "content")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "hasNext", expression = "java(logs.hasNext())")
    WebhookEventLogFilterResponse mapToFilterResponse(Page<WebhookEventLog> logs);
}