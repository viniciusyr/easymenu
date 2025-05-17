package com.easymenu.services;

import com.easymenu.dtos.ProductRecordDto;
import com.easymenu.dtos.ProductResponseDto;
import com.easymenu.dtos.ProductUpdateDto;
import com.easymenu.exceptions.ProductException;
import com.easymenu.factories.ProductFactory;
import com.easymenu.models.ProductModel;
import com.easymenu.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductFactory productFactory;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductFactory productFactory, ProductRepository productRepository) {
        this.productFactory = productFactory;
        this.productRepository = productRepository;
    }


    @Override
    public ProductResponseDto createProduct(ProductRecordDto product) {
        if(product == null){
            throw new ProductException.InvalidProductRecordException("Product is null");
        }

        if(productRepository.existsByName(product.name())){
            throw new ProductException(product.name() + " already exists");
        }

        ProductModel newProduct = productFactory.createProduct(product);

        productRepository.save(newProduct);

        return productFactory.toResponseDto(newProduct);
    }

    @Override
    public ProductResponseDto updateProduct(ProductUpdateDto product, UUID id) {
        ProductModel existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductException.ProductNotFoundException("Product not found to update"));

        if(product.name() != null && !product.name().isBlank()){
            if(productRepository.existsByName(product.name())){
                throw new ProductException.NameAlreadyExistsException("Product Name already exists");
            }
        }

        if(product.batchId() != null && !product.batchId().equals(existingProduct.getBatchId())){
            if(productRepository.existsByBatchId(product.batchId())){
                throw new ProductException.BatchAlreadyExistsException("Batch already exists");
            }
        }

        productFactory.applyUpdates(product, existingProduct);

        ProductModel updatedProduct = productRepository.save(existingProduct);

        log.info("Product updated successfully: {}", updatedProduct);

        return productFactory.toResponseDto(updatedProduct);
    }

    @Override
    public void deleteProduct(UUID id) {
        if(!productRepository.existsById(id)){
            throw new ProductException.ProductNotFoundException("Product not found to delete");
        }

        productRepository.deleteById(id);

        log.info("Product deleted successfully");

    }

    @Override
    public ProductResponseDto findProductById(UUID id) {
        return productRepository.findById(id).map(product -> {
            log.info("Product found: {}", product.getName());
            return productFactory.toResponseDto(product);
        })
                .orElseThrow(() -> new ProductException.ProductNotFoundException("Product not found!"));
    }

    @Override
    public List<ProductResponseDto> findAllProduct() {
        return productRepository.findAll().stream()
                .map(productFactory::toResponseDto)
                .toList();
    }
}
