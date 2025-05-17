package com.easymenu.dtos;

import com.easymenu.enums.OrderStatus;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderRecordDto(UUID userId,
                             List<UUID> productsId,
                             String observation) {
}
