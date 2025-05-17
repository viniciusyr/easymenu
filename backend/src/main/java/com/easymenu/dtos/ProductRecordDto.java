package com.easymenu.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductRecordDto(Long batchId,
                               String name,
                               String description,
                               BigDecimal price,
                               LocalDate validityStart,
                               LocalDate validityEnd) {
}
