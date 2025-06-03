package com.easymenu.order;

import com.easymenu.order.enums.OrderStatus;
import com.easymenu.product.ProductResponseDTO;
import com.easymenu.user.UserResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDTO extends RepresentationModel<OrderResponseDTO> {
    private UUID orderId;
    private Long orderNumber;
    private UserResponseDTO user;
    private List<ProductResponseDTO> products;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String observation;
    private Instant createdOn;
    private Instant updatedOn;

    public OrderResponseDTO(UUID orderId, Long orderNumber, UserResponseDTO user, List<ProductResponseDTO> products, OrderStatus status, BigDecimal totalAmount, String observation, Instant createdOn, Instant updatedOn) {
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
