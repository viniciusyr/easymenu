package com.easymenu.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "TB_PRODUCT")
public class ProductModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Long batchId;
    private String name;
    private String description;
    private BigDecimal price;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate validityStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate validityEnd;


    @CreationTimestamp
    private Instant createdOn;
    @UpdateTimestamp
    private Instant updatedOn;

    public ProductModel() {}

    public ProductModel(Long batchId, String name, String description, BigDecimal price, LocalDate validityStart, LocalDate validityEnd) {
        this.batchId = batchId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
    }

}
