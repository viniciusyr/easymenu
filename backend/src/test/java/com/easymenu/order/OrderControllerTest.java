package com.easymenu.order;

import com.easymenu.user.UserModel;
import com.easymenu.user.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testFindOrdersByUser() throws Exception{
        //Given
        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setName("Vinicius Rodrigues");
        user.setEmail("viniciusrodrigues@gmail.com");
        user.setPassword("03248902384");
        user.setRole(UserRole.ADMIN);


        OrderModel order = new OrderModel();
        order.setOrderId(UUID.randomUUID());
        order.setOrderNumber(100L);
        order.setUser(user);

        OrderSearchDTO searchCriteria = new OrderSearchDTO(null,
                null,
                order.getUser().getId(),
                null,
                null);

        mockMvc.perform(get("/orders/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/email-already-exists"))
                .andExpect(jsonPath("$.title").value("Email already exists"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Email already exists"));
    }
}