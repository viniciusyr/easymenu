package com.easymenu.dtos;

import com.easymenu.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDto extends RepresentationModel<OrderResponseDto> {
    private UUID orderId;
    private Long orderNumber;
    private UserResponseDto user;
    private List<ProductResponseDto> products;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String observation;
    private Instant createdOn;
    private Instant updatedOn;

    public OrderResponseDto(UUID orderId, Long orderNumber, UserResponseDto user, List<ProductResponseDto> products, OrderStatus status, BigDecimal totalAmount, String observation, Instant createdOn, Instant updatedOn) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.user = user;
        this.products = products;
        this.status = status;
        this.totalAmount = totalAmount;
        this.observation = observation;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }
}
