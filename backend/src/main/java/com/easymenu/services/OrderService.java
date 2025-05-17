package com.easymenu.services;

import com.easymenu.dtos.OrderRecordDto;
import com.easymenu.dtos.OrderResponseDto;
import com.easymenu.dtos.OrderUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    OrderResponseDto createOrder(OrderRecordDto orderDto);
    OrderResponseDto updateOrder(OrderUpdateDto orderDto, UUID orderId);
    void deleteOrder(UUID orderId);
    OrderResponseDto getOrderById(UUID orderId);
    List<OrderResponseDto> getAllOrders();
}
