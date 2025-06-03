package com.easymenu.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    boolean existsByName(String name);
    boolean existsByBatchId(Long bashId);
}
