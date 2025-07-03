package com.easymenu.product;

import com.easymenu.utils.PageResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/products")
    @Operation(summary = "Create Product", description = "Create a new product")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product details",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ProductSearchDTO.class),
                    examples = @ExampleObject(value = """
            {
              "batchId": "123456789",
              "name": "Chinese potato",
              "description": "Chinese potato, also known as Chinese artichoke or crosne",
              "price": 2.10,
              "validityStart": "2024-01-01",
              "validityEnd": "2024-12-31"
            }
            """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created. "),
            @ApiResponse(responseCode = "403", description = "Access denied."),
    })
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRecordDTO product) {
        ProductResponseDTO newProduct = productService.createProduct(product);
        newProduct.add(linkTo(methodOn(ProductController.class).findById(newProduct.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }


    @Operation(summary = "Update Product", description = "Update product")
    @Parameters({
            @Parameter(name = "batchId", description = "New batch ID (max 10 digits, numbers only)"),
            @Parameter(name = "name", description = "New product name (min 2 and max 50 characters)"),
            @Parameter(name = "description", description = "New product description (max 100 characters)"),
            @Parameter(name = "price", description = "New product price (min 0.01 and max 50000.00)"),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully updated. "),
            @ApiResponse(responseCode = "403", description = "Access denied."),
    })
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody @Valid ProductUpdateDTO productUpdateDto, @PathVariable(value = "id") UUID id) {
        ProductResponseDTO updatedProduct = productService.updateProduct(productUpdateDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @Operation(summary = "Find by ID", description = "Find a product by ID")
    @Parameters({
            @Parameter(name = "id", description = "Enter a product ID"),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product found. "),
            @ApiResponse(responseCode = "403", description = "Access denied."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable(value = "id")UUID id) {
        ProductResponseDTO product = productService.findProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Operation(summary = "Find all Products", description = "Retrieve all products available.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Products successfully retrieved."),
            @ApiResponse(responseCode = "403", description = "Access denied."),
    })
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> products = productService.findAllProduct();
        products.forEach(product -> product.add(linkTo(methodOn(ProductController.class)
                .findById(product.getId())).withSelfRel()));

        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Delete Product", description = "Delete a product by ID")
    @Parameters({
            @Parameter(name = "id", description = "Enter a product ID to delete")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product successfully deleted."),
            @ApiResponse(responseCode = "403", description = "Access denied."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product successfully deleted ");
    }

    @Operation(summary = "Search by Criteria", description = "Search products using criteria. You may use one or more criteria to search a product. ")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product search filters",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ProductSearchDTO.class),
                    examples = @ExampleObject(value = """
            {
              "name": "Potato",
              "description": "Chinese potato",
              "minAmount": 0.01,
              "maxAmount": 50000.00,
              "validityStart": "2025-01-01",
              "validityEnd": "2025-12-31"
            }
            """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Products successfully found."),
            @ApiResponse(responseCode = "403", description = "Access denied."),
    })
    @PostMapping("/products/search")
    public ResponseEntity<PageResultDTO<ProductResponseDTO>> getProductByCriteria(@RequestBody ProductSearchDTO productSearchDTO,
                                                                                  Pageable pageable){
        Page<ProductResponseDTO> products = productService.findByCriteria(productSearchDTO, pageable);
        products.forEach(product -> {
            product.add(linkTo(methodOn(ProductController.class).findById(productSearchDTO.id())).withSelfRel());
        });

        return ResponseEntity.ok(PageResultDTO.result(products));
    }
}
