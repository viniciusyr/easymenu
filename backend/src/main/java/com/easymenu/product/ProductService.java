package com.easymenu.product;

import java.util.List;
import java.util.UUID;


public interface ProductService {
    ProductResponseDto createProduct(ProductRecordDto product);
    ProductResponseDto updateProduct(ProductUpdateDto product, UUID id);
    void deleteProduct(UUID id);
    ProductResponseDto findProductById(UUID id);
    List<ProductResponseDto> findAllProduct();
}
