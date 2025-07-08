package com.easymenu.user;

import com.easymenu.user.exceptions.UserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    public void testChangeUserStatusSuccess() throws Exception {

        UUID userId = UUID.randomUUID();
        doNothing().when(userService).inactiveUser(userId);

        mockMvc.perform(put("/users/" + userId + "/deactivate")
                        .with(user("vinicius").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string("User's status was successfully changed to INACTIVE"));

    }

    @Test
    public void testActiveUserFail() throws Exception {
        UUID userId = UUID.randomUUID();

        doThrow(new UserException.UserWrongStatusException("The user's status is already ACTIVE"))
                .when(userService).inactiveUser(userId);

        mockMvc.perform(put("/users/" + userId + "/deactivate")
                        .with(user("vinicius").roles("ADMIN")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/wrong-user-status"))
                .andExpect(jsonPath("$.title").value("Wrong user status"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("The user's status is already ACTIVE"));

    }

    @Test
    public void testDeactiveUserFail() throws Exception {
        UUID userId = UUID.randomUUID();

        doThrow(new UserException.UserWrongStatusException("The user's status is already INACTIVE"))
                .when(userService).inactiveUser(userId);

        mockMvc.perform(put("/users/" + userId + "/deactivate")
                        .with(user("vinicius").roles("ADMIN")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://easymenu.app/problems/wrong-user-status"))
                .andExpect(jsonPath("$.title").value("Wrong user status"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("The user's status is already INACTIVE"));
    }

    @Test
    public void testChangeUserStatusNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        doThrow(new UserException.UserNotFoundException("User not found"))
                .when(userService).activeUser(userId);


        mockMvc.perform(put("/users/" + userId + "/active")
                        .with(user("vinicius").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

}