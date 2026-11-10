package com.example.ordertracker.service;

import com.example.ordertracker.entity.Order;
import com.example.ordertracker.entity.WebhookEventLog;
import com.example.ordertracker.exception.BusinessException;
import com.example.ordertracker.exception.ExceptionConstants;
import com.example.ordertracker.mapper.WebhookEventLogFilterResponseMapper;
import com.example.ordertracker.mapper.WebhookLogMapper;
import com.example.ordertracker.model.OrderStatus;
import com.example.ordertracker.model.WebhookEventLogSpecification;
import com.example.ordertracker.model.WebhookStatus;
import com.example.ordertracker.model.request.PagesRequest;
import com.example.ordertracker.model.request.WebhookLogFilterRequest;
import com.example.ordertracker.model.response.WebhookEventLogFilterResponse;
import com.example.ordertracker.model.response.WebhookEventLogResponse;
import com.example.ordertracker.repository.OrderRepository;
import com.example.ordertracker.repository.WebhookEventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final WebhookEventLogRepository logRepository;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final EmailService emailService;
    private final WebhookLogMapper webhookLogMapper;
    private final WebhookEventLogFilterResponseMapper webhookEventLogFilterResponseMapper;

    @Transactional
    public void handleWebhook(String provider, String rawPayload) {
        log.info("Yeni webhook qəbul edildi. Provider: {}", provider);
        String eventType = "UNKNOWN";
        try {
            JsonNode jsonNode = objectMapper.readTree(rawPayload);
            eventType = jsonNode.has("eventType") ? jsonNode.get("eventType").asText() : "UNKNOWN";
            String orderNumber = jsonNode.has("orderNumber") ? jsonNode.get("orderNumber").asText() : null;

            log.info("Webhook emal edilir. Provider: {}, EventType: {}, OrderNumber: {}", provider, eventType, orderNumber);

            Order order = orderRepository.findByOrderNumber(orderNumber)
                    .orElseThrow(() -> new BusinessException(ExceptionConstants.ORDER_NOT_FOUND));

            OrderStatus orderStatus = determineNewStatus(provider, eventType);
            order.setStatus(orderStatus);
            orderRepository.save(order);
            log.info("Sifariş statusu avtomatik yeniləndi: {} -> {}", orderNumber, orderStatus);

            emailService.sendOrderStatusUpdateEmail(
                    order.getUser().getEmail(),
                    order.getStatus().name(),
                    order.getOrderNumber()
            );

            saveLog(provider, eventType, rawPayload, WebhookStatus.SUCCESS, null);
            log.info("Webhook uğurla tamamlandı və log bazaya yazıldı. Status: SUCCESS");
        } catch (Exception e) {
            log.error("Webhook emalında xəta baş verdi. Provider: {}, EventType: {}, Səbəb: {}", provider, eventType, e.getMessage());
            saveLog(provider, eventType, rawPayload, WebhookStatus.FAILED, e.getMessage());
        }
    }

    public WebhookEventLogResponse getLogById(Long id) {
        log.info("Webhook log axtarılır. ID: {}", id);
        WebhookEventLog log = logRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionConstants.LOG_NOT_FOUND));

        return webhookLogMapper.toResponse(log);
    }

    public WebhookEventLogFilterResponse getAllLog(WebhookLogFilterRequest logFilterRequest, PagesRequest pagesRequest) {
        log.info("Webhook loglar filtr ilə sorğulanır");
        Specification<WebhookEventLog> specification = WebhookEventLogSpecification.toSpecification(logFilterRequest);
        Page<WebhookEventLog> logs = logRepository.findAll(specification, pagesRequest.toPageable());
        return webhookEventLogFilterResponseMapper.mapToFilterResponse(logs);
    }

    private OrderStatus determineNewStatus(String provider, String eventType) {
        if ("payment".equalsIgnoreCase(provider)) {
            if ("payment.succeeded".equalsIgnoreCase(eventType)) {
                return OrderStatus.PAID;
            } else {
                return OrderStatus.PAYMENT_FAILED;
            }
        }
        if ("shipment".equalsIgnoreCase(provider)) {
            if ("shipment.delivered".equalsIgnoreCase(eventType)) {
                return OrderStatus.DELIVERED;
            } else {
                return OrderStatus.SHIPPED;
            }
        }
        throw new IllegalArgumentException("Naməlum provider: " + provider);
    }

    private void saveLog(String provider, String eventType, String rawPayload, WebhookStatus status, String errorMessage) {
        WebhookEventLog log = WebhookEventLog.builder()
                .provider(provider.toUpperCase())
                .eventType(eventType)
                .payload(rawPayload)
                .status(status)
                .receivedAt(LocalDateTime.now())
                .errorMessage(errorMessage)
                .build();
        logRepository.save(log);
    }
}
