package com.easymenu.product;

import com.easymenu.product.exceptions.ProductException;
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
    public ProductResponseDTO createProduct(ProductRecordDTO product) {
        if(product == null){
            throw new ProductException.ProductNotFoundException("ProductRecordDTO is null");
        }

        if(productRepository.existsByName(product.name())){
            throw new ProductException.NameAlreadyExistsException(product.name() + " already exists");
        }

        ProductModel newProduct = productFactory.createProduct(product);

        productRepository.save(newProduct);

        return productFactory.toResponseDto(newProduct);
    }

    @Override
    public ProductResponseDTO updateProduct(ProductUpdateDTO product, UUID id) {
        ProductModel existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductException.ProductNotFoundException("Product not found to update! ID: " + id));

        if(product.name() != null && !product.name().isBlank()){
            if(productRepository.existsByName(product.name())){
                throw new ProductException.NameAlreadyExistsException("Product Name already exists: " + product.name() );
            }
        }

        if(product.batchId() != null && !product.batchId().equals(existingProduct.getBatchId())){
            if(productRepository.existsByBatchId(product.batchId())){
                throw new ProductException.InvalidBatchException("Batch ID is the same: " + product.batchId());
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
    public ProductResponseDTO findProductById(UUID id) {
        return productRepository.findById(id).map(product -> {
            log.info("Product found: {}", product.getName());
            return productFactory.toResponseDto(product);
        })
                .orElseThrow(() -> new ProductException.ProductNotFoundException("Product not found!"));
    }

    @Override
    public List<ProductResponseDTO> findAllProduct() {
        return productRepository.findAll().stream()
                .map(productFactory::toResponseDto)
                .toList();
    }

}
