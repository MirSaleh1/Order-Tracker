package com.example.ordertracker.model.request;

import java.math.BigDecimal;

public record UpdateOrderRequest(Long orderId,
                                 String description,
                                 BigDecimal totalPrice) {
}
