package com.easymenu.order;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    OrderResponseDTO createOrder(OrderRecordDTO orderDto);
    OrderResponseDTO updateOrder(OrderUpdateDTO orderDto, UUID orderId);
    OrderResponseDTO getOrderById(UUID orderId);
    List<OrderResponseDTO> getAllOrders();
    Page<OrderResponseDTO> findByCriteria(OrderSearchDTO searchDto, Pageable pageable);
}
