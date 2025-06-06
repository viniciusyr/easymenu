package com.easymenu.authentication;

import com.easymenu.user.UserRecordDTO;
import com.easymenu.user.UserResponseDTO;
import com.easymenu.user.UserService;
import com.easymenu.authentication.AuthenticationRecordDTO;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterSuccess() throws Exception {
        UserRecordDTO recordDTO = new UserRecordDTO("testuser", "test@example.com", "password", UserRole.USER);
        UserResponseDTO responseDTO = new UserResponseDTO(UUID.randomUUID(), "testuser", "test@example.com", UserStatus.ACTIVE, UserRole.USER);

        when(userService.createUser(any(UserRecordDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testEmailAlreadyExists() throws Exception{

        UserRecordDTO recordDTO = new UserRecordDTO("testuser2", "test@example.com", "password", UserRole.USER);

        when(userService.createUser(any(UserRecordDTO.class)))
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

        when(userService.findByName("testuser2"))
                .thenThrow(new UserException.UserNotFoundException("Username not found"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/user-not-found"))
                .andExpect(jsonPath("$.title").value("User not found"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Username not found"));
    }
}