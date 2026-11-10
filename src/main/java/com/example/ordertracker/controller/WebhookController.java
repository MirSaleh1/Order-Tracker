package com.example.ordertracker.controller;

import com.example.ordertracker.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/payment")
    public ResponseEntity<String> handlePaymentWebhook(@RequestBody String payload) {
        webhookService.handleWebhook("payment", payload);
        return ResponseEntity.ok("Payment webhook processed successfully");
    }

    @PostMapping("/shipment")
    public ResponseEntity<String> handleShipmentWebhook(@RequestBody String payload) {
        webhookService.handleWebhook("shipment", payload);
        return ResponseEntity.ok("Shipment webhook processed successfully");
    }
}
