package com.example.ordertracker.controller;

import com.example.ordertracker.model.request.PagesRequest;
import com.example.ordertracker.model.request.WebhookLogFilterRequest;
import com.example.ordertracker.model.response.WebhookEventLogFilterResponse;
import com.example.ordertracker.model.response.WebhookEventLogResponse;
import com.example.ordertracker.service.WebhookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/webhook-logs")
@RequiredArgsConstructor
public class AdminWebhookLogController {

    private final WebhookService webhookService;

    @GetMapping
    public ResponseEntity<WebhookEventLogFilterResponse> getAllLogs(
            @ModelAttribute WebhookLogFilterRequest filterRequest,
            @Valid @ModelAttribute PagesRequest pagesRequest) {
        return ResponseEntity.ok(webhookService.getAllLog(filterRequest, pagesRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebhookEventLogResponse> getLogById(@PathVariable Long id) {
        return ResponseEntity.ok(webhookService.getLogById(id));
    }
}
