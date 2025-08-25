package com.easymenu.payment;

import com.easymenu.user.UserModel;
import com.easymenu.user.UserService;
import com.easymenu.user.enums.UserRole;
import com.easymenu.user.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void requestPayment() throws Exception{
        UserModel userModelTest = new UserModel("testuser",
                "testuser@email.com" ,
                "testpassword",
                UserStatus.ACTIVE,
                UserRole.USER);

        PaymentRecordDTO paymentRecordTest = createPayment(userModelTest);
        PaymentResponseDTO paymentResponseTest = new PaymentResponseDTO(UUID.randomUUID(),
                paymentRecordTest.transactionId(),
                paymentRecordTest.user(),
                paymentRecordTest.amount(),
                paymentRecordTest.currency().name(),
                paymentRecordTest.description(),
                paymentRecordTest.provider(),
                paymentRecordTest.userEmail(),
                paymentRecordTest.paymentStatus().name(),
                Instant.now());

        when(userService.findModelByUsername("testuser")).thenReturn(userModelTest);

        when(paymentService.processPayment(any(PaymentRecordDTO.class), any(UserDetails.class)))
                .thenReturn(paymentResponseTest);

        mockMvc.perform(post("/payment/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRecordTest))
                .with(user("testuser").roles("USER")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.provider").value("STRIPE"));
    }

    @Test
    void inputValidationTest(){

    }

    @Test
    void securityTest(){

    }

    @Test
    void exceptionHandlingTest(){

    }

    @Test
    void integrationTest(){

    }

    @Test
    void multipleRequestsTest(){

    }

    PaymentRecordDTO createPayment(UserModel user){
        PaymentRecordDTO dto = new PaymentRecordDTO(
                "test-id",
                user,
                1000L,
                PaymentCurrency.USD,
                "description-test",
                PaymentProvider.STRIPE,
                "user@testemail.com",
                PaymentStatus.PENDING
        );
        return dto;
    }
}