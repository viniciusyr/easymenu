package com.easymenu.order;

import com.easymenu.order.enums.OrderStatus;
import com.easymenu.product.ProductResponseDTO;
import com.easymenu.user.UserResponseDTO;
import com.easymenu.order.exceptions.OrderException;
import com.easymenu.product.ProductModel;
import com.easymenu.user.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderFactory {

    public OrderModel createOrder(Long orderNumber,
                                  UserModel user,
                                  List<ProductModel> products,
                                  BigDecimal totalAmount,
                                  String observation) {

        return new OrderModel(
                orderNumber,
                user,
                products,
                OrderStatus.PENDING,
                totalAmount,
                observation
        );
    }

    public void updateOrder(OrderUpdateDTO updates, OrderModel existingOrder) {
        if (updates == null) {
            throw new OrderException.UpdateNotFoundException("OrderUpdateDto is null");
        }

        if (existingOrder == null) {
            throw new OrderException.OrderNotFoundException("Existing product is null");
        }

        if(updates.products() != null){
            existingOrder.setProducts(updates.products());
        }

        if(updates.status() != null){
            existingOrder.setStatus(updates.status());
        }

        if(updates.observation() != null){
            existingOrder.setObservation(updates.observation());
        }

        existingOrder.setUpdatedOn(Instant.now());

        log.info("Updating order: {}", existingOrder);

    }

    public OrderResponseDTO toResponseDto(OrderModel existingOrder){
        UserResponseDTO userDto = toUserResponseDto(existingOrder.getUser());
        List<ProductResponseDTO> productDto = new ArrayList<>(existingOrder.getProducts().stream()
                .map(this::toProductResponseDto)
                .toList());

        return new OrderResponseDTO(
                existingOrder.getOrderId(),
                existingOrder.getOrderNumber(),
                userDto,
                productDto,
                existingOrder.getStatus(),
                existingOrder.getTotalAmount(),
                existingOrder.getObservation(),
                existingOrder.getCreatedOn(),
                existingOrder.getUpdatedOn()
                );
    }

    private UserResponseDTO toUserResponseDto(UserModel user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getStatus(),
                user.getRole()
        );
    }

    private ProductResponseDTO toProductResponseDto(ProductModel product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

}
