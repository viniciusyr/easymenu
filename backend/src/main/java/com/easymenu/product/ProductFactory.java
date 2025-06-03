package com.easymenu.product;

import com.easymenu.product.exceptions.ProductException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class ProductFactory {
    public ProductModel createProduct(ProductRecordDTO productRecordDto) {
        if(productRecordDto == null){
            throw new ProductException.ProductNotFoundException("ProductRecordDTO is null");
        }

        return new ProductModel(productRecordDto.batchId(),
                productRecordDto.name(),
                productRecordDto.description(),
                productRecordDto.price(),
                productRecordDto.validityStart(),
                productRecordDto.validityEnd());
    }

    public void applyUpdates(ProductUpdateDTO updates, ProductModel existingProduct) {
        if(updates == null){
            throw new ProductException.ProductNotFoundException("ProductUpdateDTO is null");
        }

        if(existingProduct == null) {
            throw new ProductException.ProductNotFoundException("Existing product is null");
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

    public ProductResponseDTO toResponseDto(ProductModel product){
        return new ProductResponseDTO(product.getId(),
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
