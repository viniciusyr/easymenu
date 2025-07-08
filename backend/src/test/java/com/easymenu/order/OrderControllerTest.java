package com.easymenu.order;

import com.easymenu.order.enums.OrderStatus;
import com.easymenu.user.UserFactory;
import com.easymenu.user.UserModel;
import com.easymenu.user.enums.UserRole;
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
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFactory userFactory;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    ObjectMapper objectMapper;

    private OrderResponseDTO createOrder(UUID userId) {
        UserModel user = new UserModel();
        user.setId(userId);
        user.setName("Vinicius Rodrigues");
        user.setEmail("viniciusrodrigues@gmail.com");
        user.setRole(UserRole.ADMIN);

        return new OrderResponseDTO(
                UUID.randomUUID(),
                100L,
                userFactory.toResponseDto(user),
                Collections.emptyList(),
                OrderStatus.APPROVED,
                new BigDecimal("500.00"),
                "test order",
                Instant.now(),
                null
        );
    }

    @Test
    void testValidSearch() throws Exception {
        UUID userId = UUID.randomUUID();
        OrderResponseDTO order = createOrder(userId);

        when(orderService.findByCriteria(any(), any()))
                .thenReturn(new PageImpl<>(Collections.singletonList(order)));

        OrderSearchDTO dto = new OrderSearchDTO(
                null, null, userId, null,
                null, null, null, null, null, null
        );

        mockMvc.perform(get("/orders/search")
                        .with(user("vinicius").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].user.id").value(userId.toString()));
    }

    @Test
    void testNoOrdersFound() throws Exception {
        when(orderService.findByCriteria(any(), any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        OrderSearchDTO dto = new OrderSearchDTO(
                null, null, UUID.randomUUID(), null,
                null, null, null, null, null, null
        );

        mockMvc.perform(get("/orders/search")
                        .with(user("testUser").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void testUserNoPermission() throws Exception {
        OrderSearchDTO dto = new OrderSearchDTO(
                null, null, UUID.randomUUID(), null,
                null, null,null, null, null, null
        );

        mockMvc.perform(get("/orders/search")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInternalError() throws Exception {
        when(orderService.findByCriteria(any(), any()))
                .thenThrow(new RuntimeException("Test error"));

        OrderSearchDTO dto = new OrderSearchDTO(
                null, null, UUID.randomUUID(), null,
                null, null,null, null, null, null
        );

        mockMvc.perform(get("/orders/search")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/internal-error"))
                .andExpect(jsonPath("$.title").value("Internal Error"));


    }
}