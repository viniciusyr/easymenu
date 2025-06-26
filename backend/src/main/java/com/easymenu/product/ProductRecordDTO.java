package com.easymenu.product;

import com.easymenu.product.annotations.ValidDateRange;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ValidDateRange
public record ProductRecordDTO(@NotNull(message = "The batchId is required.")
                               @Max(value = 9999999999L, message = "batchId must have maximum of 10 digits")
                               Long batchId,

                               @NotBlank(message = "Name is required")
                               @Size(min=2, max = 50, message = "Name must have maximum of 20 characters")
                               String name,

                               @NotBlank(message = "Description is required")
                               @Size(max = 100, message = "Name must have maximum of 20 characters.")
                               String description,

                               @NotNull(message = "Price is required")
                               @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than zero")
                               @DecimalMax(value = "50000.00", inclusive = true, message = "Price must be lower than 50K")
                               BigDecimal price,

                               @NotNull(message = "Validity start date is required")
                               LocalDate validityStart,

                               @NotNull(message = "Validity end date is required")
                               LocalDate validityEnd) {
}
