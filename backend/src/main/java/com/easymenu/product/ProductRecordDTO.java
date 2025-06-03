package com.easymenu.product;

import jakarta.validation.constraints.Max;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductRecordDTO(@Max(20) Long batchId,
                               @Max(20) String name,
                               @Max(100) String description,
                               BigDecimal price,
                               LocalDate validityStart,
                               LocalDate validityEnd) {
}
