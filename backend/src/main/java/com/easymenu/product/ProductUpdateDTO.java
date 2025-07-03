package com.easymenu.product;

import com.easymenu.product.validations.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Schema(name = "ProductUpdate")
@ValidDateRange
public record ProductUpdateDTO(
        @Max(value = 9999999999L, message = "batchId must have maximum of 10 digits")
        Long batchId,

        @Size(min=2, max = 50, message = "Name must have maximum of 50 characters")
        String name,

        @Size(max = 100, message = "Description must have maximum of 100 characters.")
        String description,

        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than zero")
        @DecimalMax(value = "50000.00", inclusive = true, message = "Price must be lower than 50K")
        BigDecimal price,
        LocalDate validityStart,
        LocalDate validityEnd,
        Instant createdOn,
        Instant updatedOn
) {}