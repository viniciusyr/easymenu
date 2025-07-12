package com.easymenu.product;

import com.easymenu.product.exceptions.ProductException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateProduct() throws Exception {
        ProductRecordDTO productTest = new ProductRecordDTO(100L,
                "Burger",
                "Cheeseburger",
                new BigDecimal("25.50"),
                LocalDate.now(),
                LocalDate.of(2026, 3, 25));
        ProductResponseDTO product = new ProductResponseDTO(UUID.randomUUID(), "Burger", new BigDecimal("25.50"));
        
        when(productService.createProduct(any(ProductRecordDTO.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTest))
                        .with(user("vinicius").roles("ADMIN")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.price").value(25.50));
    }

    @Test
    void testInvalidInput() throws Exception {
        String invalidJson = """
        {
          "name": "",
          "description": "",
          "price": -5,
          "status": "AVAILABLE"
        }
        """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
                        .with(user("vinicius").roles("ADMIN")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/validation-error"))
                .andExpect(jsonPath("$.violations").isArray());
    }

    @Test
    void testFindById() throws Exception {
        UUID id = UUID.randomUUID();
        ProductResponseDTO product = new ProductResponseDTO(id, "Burger", new BigDecimal("30.00"));

        when(productService.findProductById(id)).thenReturn(product);

        mockMvc.perform(get("/products/" + id)
                        .with(user("vinicius").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Burger"));
    }

    @Test
    void testProductNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(productService.findProductById(id)).thenThrow(new ProductException.ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/products/" + id)
                        .with(user("vinicius").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/product-not-found"))
                .andExpect(jsonPath("$.title").value("Product not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testValidSearch() throws Exception {
        UUID id = UUID.randomUUID();
        ProductResponseDTO product = new ProductResponseDTO(id, "Burger", new BigDecimal("30.00"));

        when(productService.findByCriteria(any(), any()))
                .thenReturn(new PageImpl<>(Collections.singletonList(product)));

        ProductSearchDTO dto = new ProductSearchDTO(id, null, null, null,
                null, null, null, null, null, null, null);

        mockMvc.perform(post("/products/search")
                        .with(user("vinicius").roles("ADMIN"))
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Burger"));
    }
}
