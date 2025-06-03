package com.easymenu.product;

import java.util.List;
import java.util.UUID;


public interface ProductService {
    ProductResponseDTO createProduct(ProductRecordDTO product);
    ProductResponseDTO updateProduct(ProductUpdateDTO product, UUID id);
    void deleteProduct(UUID id);
    ProductResponseDTO findProductById(UUID id);
    List<ProductResponseDTO> findAllProduct();
}
