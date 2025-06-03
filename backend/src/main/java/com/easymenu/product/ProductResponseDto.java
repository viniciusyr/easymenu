package com.easymenu.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ProductResponseDto extends RepresentationModel<ProductResponseDto> {

    private UUID id;
    private Long batchId;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate validityStart;
    private LocalDate validityEnd;
    private Instant createdOn;
    private Instant updatedOn;

    public ProductResponseDto(UUID id, Long batchId, String name, String description, BigDecimal price, LocalDate validityStart, LocalDate validityEnd, Instant createdOn, Instant updatedOn) {
        this.id = id;
        this.name = name;
        this.batchId = batchId;
        this.description = description;
        this.price = price;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    public ProductResponseDto(UUID id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
