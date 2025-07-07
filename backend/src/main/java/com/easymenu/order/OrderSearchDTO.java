package com.easymenu.order;

import com.easymenu.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record OrderSearchDTO(
        UUID orderId,
        Long orderNumber,
        UUID userId,
        OrderStatus status,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        String observation,
        LocalDate startDate,
        LocalDate endDate
) {

}
