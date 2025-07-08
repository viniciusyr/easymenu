package com.easymenu.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "ProductSearch", description = "Payload to search a product")
public record ProductSearchDTO(
        @Schema(description = "Batch ID (max 10 digits)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Batch ID (max 10 digits)", example = "123456789")
        @Max(value = 9999999999L, message = "batchId must have maximum of 10 digits")
        Long batchId,

        @Schema(description = "Name of the product", example = "Tomato")
        @Size(min=2, max = 50, message = "Name must have minimum 0f 2 and maximum of 20 characters")
        String name,

        @Schema(description = "Short description", example = "Fresh red tomato")
        @Size(max = 100, message = "Name must have maximum of 100 characters.")
        String description,

        @Schema(description = "Minimum price", example = "2.50")
        @DecimalMin(value = "0.01")
        @DecimalMax(value = "50000.00")
        BigDecimal minAmount,

        @Schema(description = "Maximum price", example = "50000.00")
        @DecimalMin(value = "0.01")
        @DecimalMax(value = "50000.00")
        BigDecimal maxAmount,

        @Schema(description = "Star date of product validity", example = "2025-01-31")
        LocalDate validityStart,

        @Schema(description = "End date of product validity", example = "2025-02-11")
        LocalDate validityEnd,

        @Schema(description = "Star date of product creation", example = "2025-01-31")
        LocalDate startDate,

        @Schema(description = "End date of product creation", example = "2025-01-31")
        LocalDate endDate,

        @Schema(description = "Exact update date of product", example = "2025-07-08")
        LocalDate updatedOn
) {}
