package com.example.ordertracker.model.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public record OrderResponse(
        Long id,
        String description,
        BigDecimal totalPrice,
        String status,
        LocalDateTime createdAt,
        String orderNumber
) {}