package com.example.ordertracker.model.request;

import java.math.BigDecimal;

public record CreateOrderRequest(
        Long userId,
        String description,
        BigDecimal totalPrice
) {}