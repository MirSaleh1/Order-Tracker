package com.example.ordertracker.mapper;

import com.example.ordertracker.entity.Order;
import com.example.ordertracker.model.response.OrderResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponseList(Set<Order> orders);
}
