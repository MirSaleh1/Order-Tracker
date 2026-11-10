package com.example.ordertracker.controller;

import com.example.ordertracker.model.request.CreateOrderRequest;
import com.example.ordertracker.model.request.UpdateOrderRequest;
import com.example.ordertracker.model.response.OrderResponse;
import com.example.ordertracker.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @PutMapping
    public ResponseEntity<OrderResponse> updateOrder(@Valid @RequestBody UpdateOrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(request));
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByOrderNumber(orderNumber));
    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderNumber) {
        orderService.deleteOrder(orderNumber);
        return ResponseEntity.noContent().build();
    }
}
