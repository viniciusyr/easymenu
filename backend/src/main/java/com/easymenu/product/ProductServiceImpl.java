package com.easymenu.product;

import com.easymenu.product.exceptions.ProductException;
import com.easymenu.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductFactory productFactory;
    private final ProductRepository productRepository;
    private final RedisService regisService;
    private static final String PRODUCT_CACHE_KEY = "all-products";

    public ProductServiceImpl(ProductFactory productFactory,
                              ProductRepository productRepository,
                              RedisService regisService) {
        this.productFactory = productFactory;
        this.productRepository = productRepository;
        this.regisService = regisService;
    }

    @Override
    @CachePut(value="PRODUCT_CACHE", key="#result.id")
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
    @CachePut(value="PRODUCT_CACHE", key="#result.id")
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
    @CacheEvict(value="PRODUCT_CACHE", key="#productId")
    public void deleteProduct(UUID id) {
        if(!productRepository.existsById(id)){
            throw new ProductException.ProductNotFoundException("Product not found to delete");
        }

        productRepository.deleteById(id);

        log.info("Product deleted successfully");

    }

    @Override
    @Cacheable(value="PRODUCT_CACHE", key ="#id")
    public ProductResponseDTO findProductById(UUID id) {
        return productRepository.findById(id).map(productFactory::toResponseDto)
                .orElseThrow(() -> new ProductException.ProductNotFoundException("Product not found!"));
    }

    @Override
    public List<ProductResponseDTO> findAllProduct() {
        return regisService.getList(PRODUCT_CACHE_KEY, ProductResponseDTO.class)
                .orElseGet(() -> {
                    List<ProductResponseDTO> products = productRepository.findAll()
                            .stream()
                            .map(productFactory::toResponseDto)
                            .toList();
                    regisService.set(PRODUCT_CACHE_KEY, products, Duration.ofMinutes(10));
                    return products;
                });
    }

    @Override
    @Cacheable(value="PRODUCT_CACHE",
            key="'search:' + #searchDTO.name() + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<ProductResponseDTO> findByCriteria(ProductSearchDTO searchDTO, Pageable pageable) {
        if (searchDTO == null){
            throw new ProductException.InvalidFilterException("SearchDTO is null") ;
        }

        Specification<ProductModel> spec = Specification.where(null);

        if(searchDTO.id() != null){
            spec = spec.and(ProductSpecs.hasId(searchDTO.id()));
        }

        if(searchDTO.batchId() != null){
            spec = spec.and(ProductSpecs.hasBatchId(searchDTO.batchId()));
        }

        if(searchDTO.name() != null){
            spec = spec.and(ProductSpecs.containsName(searchDTO.name()));
        }

        if(searchDTO.description() != null){
            spec = spec.and(ProductSpecs.containsDescription(searchDTO.description()));
        }

        if(searchDTO.minAmount() != null){
            if(searchDTO.maxAmount() != null){
                spec = spec.and(ProductSpecs.betweenPrice(searchDTO.minAmount(), searchDTO.maxAmount()));
            } else {
                spec = spec.and(ProductSpecs.betweenPrice(searchDTO.minAmount(), BigDecimal.valueOf(50000)));
            }
        }

        if(searchDTO.validityEnd() != null){
            spec = spec.and(ProductSpecs.hasEndValidation(searchDTO.validityEnd()));
        }

        if (searchDTO.startDate() != null && searchDTO.endDate() != null) {
            spec = spec.and(ProductSpecs.betweenDates(searchDTO.startDate(), searchDTO.endDate()));
        } else if (searchDTO.startDate() != null) {
            spec = spec.and(ProductSpecs.createdAfter(searchDTO.startDate()));
        } else if (searchDTO.endDate() != null) {
            spec = spec.and(ProductSpecs.createdBefore(searchDTO.endDate()));
        }

        if (searchDTO.updatedOn() != null) {
            spec = spec.and(ProductSpecs.updatedOn(searchDTO.updatedOn()));
        }

        List<ProductModel> result = productRepository.findAll(spec);

        return new PageImpl<>(new ArrayList<>(result.stream().map(productFactory::toResponseDto).toList()));
    }

}
