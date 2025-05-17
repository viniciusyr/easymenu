package com.easymenu.services;

import com.easymenu.dtos.ProductRecordDto;
import com.easymenu.dtos.ProductResponseDto;
import com.easymenu.dtos.ProductUpdateDto;

import java.util.List;
import java.util.UUID;


public interface ProductService {
    ProductResponseDto createProduct(ProductRecordDto product);
    ProductResponseDto updateProduct(ProductUpdateDto product, UUID id);
    void deleteProduct(UUID id);
    ProductResponseDto findProductById(UUID id);
    List<ProductResponseDto> findAllProduct();
}
