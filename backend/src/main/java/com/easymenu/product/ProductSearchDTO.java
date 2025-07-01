package com.easymenu.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ProductSearchDTO(
        UUID id,
        Long batchId,
        String name,
        String description,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        LocalDate validityStart,
        LocalDate validityEnd,
        LocalDate startDate,
        LocalDate endDate
) {}
