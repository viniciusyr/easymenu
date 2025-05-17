package com.easymenu.factories;

import com.easymenu.dtos.ProductRecordDto;
import com.easymenu.dtos.ProductResponseDto;
import com.easymenu.dtos.ProductUpdateDto;
import com.easymenu.exceptions.ProductException;
import com.easymenu.models.ProductModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class ProductFactory {
    public ProductModel createProduct(ProductRecordDto productRecordDto) {
        if(productRecordDto == null){
            throw new ProductException.InvalidProductRecordException("ProductRecordDto is null");
        }

        return new ProductModel(productRecordDto.batchId(),
                productRecordDto.name(),
                productRecordDto.description(),
                productRecordDto.price(),
                productRecordDto.validityStart(),
                productRecordDto.validityEnd());
    }

    public void applyUpdates(ProductUpdateDto updates, ProductModel existingProduct) {
        if(updates == null){
            throw new ProductException.InvalidProductUpdateException("ProductUpdateDto is null");
        }

        if(existingProduct == null) {
            throw new ProductException.InvalidProductException("Existing product is null");
        }

        if(updates.batchId() != null) {
            existingProduct.setBatchId(updates.batchId());
        }

        if(updates.name() != null) {
            existingProduct.setName(updates.name());
        }

        if(updates.description() != null) {
            existingProduct.setDescription(updates.description());
        }

        if(updates.validityStart() != null) {
            existingProduct.setValidityStart(updates.validityStart());
        }

        if(updates.validityEnd() != null) {
            existingProduct.setValidityEnd(updates.validityEnd());
        }

        existingProduct.setUpdatedOn(Instant.now());

        log.info("Updating product: {}", existingProduct);

    }

    public ProductResponseDto toResponseDto(ProductModel product){
        if(product.getId() == null){
            throw new ProductException.InvalidProductException("Product ID is null on toResponseDto");
        }

        return new ProductResponseDto(product.getId(),
                product.getBatchId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getValidityStart(),
                product.getValidityEnd(),
                product.getCreatedOn(),
                product.getUpdatedOn());

    }
}
