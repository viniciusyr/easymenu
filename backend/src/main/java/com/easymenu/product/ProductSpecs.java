package com.easymenu.product;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ProductSpecs {
    public static Specification<ProductModel> hasId(UUID providedId){
        return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<ProductModel> hasBatchId(Long providedBatchId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("batchId"), providedBatchId);
    }

    public static Specification<ProductModel> containsName(String providedName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + providedName.toLowerCase() + "%");
    }

    public static Specification<ProductModel> containsDescription(String providedDescription){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + providedDescription.toLowerCase() + "%");
    }

    public static Specification<ProductModel> betweenPrice(BigDecimal minAmount, BigDecimal maxAmount){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minAmount, maxAmount);
    }

    public static Specification<ProductModel> hasEndValidation(LocalDate end){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("validityEnd"), end.atStartOfDay());
    }

    public static Specification<ProductModel> betweenDates(LocalDate start, LocalDate end){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdOn"), start.atStartOfDay(), end.atTime(23, 59, 59));
    }
}
