package com.easymenu.authentication;

import com.easymenu.user.UserRecordDTO;
import com.easymenu.user.UserResponseDTO;
import com.easymenu.user.UserService;
import com.easymenu.user.enums.UserRole;
import com.easymenu.user.enums.UserStatus;
import com.easymenu.user.exceptions.UserException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginSuccess() throws Exception {
        AuthenticationRecordDTO dto = new AuthenticationRecordDTO("testuser", "password");

        LoginResponseDTO response = new LoginResponseDTO("fake-token");

        when(authenticationService.login(any(AuthenticationRecordDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-token"));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        UserRecordDTO recordDTO = new UserRecordDTO("testuser", "test@example.com", "password", UserRole.USER);
        UserResponseDTO responseDTO = new UserResponseDTO(UUID.randomUUID(), "testuser", "test@example.com", UserStatus.ACTIVE, UserRole.USER);

        when(authenticationService.register(any(UserRecordDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testRegisterValidationError() throws Exception {

        String invalidJson = """
        {
          "name": "",
          "email": "oeiuqwoiquweoi",
          "password": "",
          "role": "USER"
        }
        """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/validation-error"))
                .andExpect(jsonPath("$.title").value("Validation Failed"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations.length()").value(4));
    }

    @Test
    public void testEmailAlreadyExists() throws Exception{

        UserRecordDTO recordDTO = new UserRecordDTO("testuser2", "test@example.com", "password", UserRole.USER);

        when(authenticationService.register(any(UserRecordDTO.class)))
                .thenThrow(new UserException.EmailAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(recordDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/email-already-exists"))
                .andExpect(jsonPath("$.title").value("Email already exists"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Email already exists"));
    }

    @Test
    public void testUsernameNotFound() throws Exception{

        AuthenticationRecordDTO dto = new AuthenticationRecordDTO("testuser2", "password");

        when(authenticationService.login(dto))
                .thenThrow(new UserException.UserNotFoundException("Username not found"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/user-not-found"))
                .andExpect(jsonPath("$.title").value("User not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Username not found"));
    }
}