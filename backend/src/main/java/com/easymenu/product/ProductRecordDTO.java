package com.easymenu.product;

import com.easymenu.product.validations.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ValidDateRange
@Schema(name = "ProductRequest", description = "Payload to create a product")
public record ProductRecordDTO(
        @Schema(description = "Batch ID (max 10 digits)", example = "123456")
        @NotNull(message = "The batchId is required.")
        @Max(value = 9999999999L, message = "batchId must have maximum of 10 digits")
        Long batchId,

        @Schema(description = "Name of the product", example = "Tomato")
        @NotBlank(message = "Name is required")
        @Size(min=2, max = 50, message = "Name must have maximum of 20 characters")
        String name,

        @Schema(description = "Short description", example = "Fresh red tomato")
        @NotBlank(message = "Description is required")
        @Size(max = 100, message = "Name must have maximum of 20 characters.")
        String description,

        @Schema(description = "Product price", example = "4.50")
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than zero")
        @DecimalMax(value = "50000.00", inclusive = true, message = "Price must be lower than 50K")
        BigDecimal price,

        @Schema(description = "Star date of product validity", example = "2025-01-31")
        @NotNull(message = "Validity start date is required")
        LocalDate validityStart,

        @Schema(description = "End date of product validity", example = "2025-02-11")
        @NotNull(message = "Validity end date is required")
        LocalDate validityEnd) {
}
