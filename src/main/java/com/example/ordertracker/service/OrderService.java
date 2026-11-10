package com.example.ordertracker.service;

import com.example.ordertracker.entity.Order;
import com.example.ordertracker.entity.User;
import com.example.ordertracker.exception.BusinessException;
import com.example.ordertracker.exception.ExceptionConstants;
import com.example.ordertracker.mapper.OrderMapper;
import com.example.ordertracker.model.OrderStatus;
import com.example.ordertracker.model.request.CreateOrderRequest;
import com.example.ordertracker.model.request.UpdateOrderRequest;
import com.example.ordertracker.model.response.OrderResponse;
import com.example.ordertracker.repository.OrderRepository;
import com.example.ordertracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderResponse createOrder(CreateOrderRequest cor) {
        log.info("Yeni sifariş yaradılır. İstifadəçi ID: {}", cor.userId());
        User user = userRepository.findById(cor.userId())
                .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));
        Order order = Order.builder().description(cor.description())
                        .user(user).totalPrice(cor.totalPrice())
                        .status(OrderStatus.PENDING).orderNumber(UUID.randomUUID().toString()).build();
        orderRepository.save(order);
        log.info("Sifariş uğurla yaradıldı. Sifariş nömrəsi: {}", order.getOrderNumber());
        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse updateOrder(UpdateOrderRequest uptadeOrderRequest) {
        log.info("Sifariş yenilənir. Sifariş ID: {}", uptadeOrderRequest.orderId());
        Order order= orderRepository.findById(uptadeOrderRequest.orderId())
                .orElseThrow(() -> new BusinessException(ExceptionConstants.ORDER_NOT_FOUND));
        order.setTotalPrice(uptadeOrderRequest.totalPrice());
        order.setDescription(uptadeOrderRequest.description());
        orderRepository.save(order);
        log.info("Sifariş uğurla yeniləndi. Sifariş nömrəsi: {}", order.getOrderNumber());
        return orderMapper.toOrderResponse(order);
    }

    public void deleteOrder(String orderNumber) {
        log.info("Sifariş silinir. Sifariş nömrəsi: {}", orderNumber);
        Order order= orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new BusinessException(ExceptionConstants.ORDER_NOT_FOUND));
        orderRepository.delete(order);
        log.info("Sifariş uğurla silindi. Sifariş nömrəsi: {}", orderNumber);
    }

    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        log.info("Sifariş axtarılır. Sifariş nömrəsi: {}", orderNumber);
        Order order= orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new BusinessException(ExceptionConstants.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }
}
