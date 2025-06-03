package com.easymenu.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record ProductUpdateDTO(Long batchId,
                               String name,
                               String description,
                               BigDecimal price,
                               LocalDate validityStart,
                               LocalDate validityEnd,
                               Instant createdOn,
                               Instant updatedOn) {
}
