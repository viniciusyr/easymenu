package com.easymenu.order;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    OrderResponseDTO createOrder(OrderRecordDTO orderDto);
    OrderResponseDTO updateOrder(OrderUpdateDTO orderDto, UUID orderId);
    void deleteOrder(UUID orderId);
    OrderResponseDTO getOrderById(UUID orderId);
    List<OrderResponseDTO> getAllOrders();
}
