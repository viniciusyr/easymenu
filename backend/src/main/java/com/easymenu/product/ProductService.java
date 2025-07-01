package com.easymenu.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface ProductService {
    ProductResponseDTO createProduct(ProductRecordDTO product);
    ProductResponseDTO updateProduct(ProductUpdateDTO product, UUID id);
    void deleteProduct(UUID id);
    ProductResponseDTO findProductById(UUID id);
    List<ProductResponseDTO> findAllProduct();
    Page<ProductResponseDTO> findByCriteria(ProductSearchDTO searchDTO, Pageable pageable);
}
