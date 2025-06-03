package com.easymenu.product;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRecordDTO product) {
            ProductResponseDTO newProduct = productService.createProduct(product);
            newProduct.add(linkTo(methodOn(ProductController.class).findById(newProduct.getId())).withSelfRel());
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody @Valid ProductUpdateDTO productUpdateDto, @PathVariable(value = "id") UUID id) {
        ProductResponseDTO updatedProduct = productService.updateProduct(productUpdateDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable(value = "id")UUID id) {
        ProductResponseDTO product = productService.findProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> products = productService.findAllProduct();
        products.forEach(product -> product.add(linkTo(methodOn(ProductController.class)
                .findById(product.getId())).withSelfRel()));

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted succesfully");
    }
}
