package com.easymenu.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "AuthenticationRequest", description = "Payload to login request")
public record AuthenticationRecordDTO(
        @Schema(description = "Username", example = "gabriel123")
        @NotEmpty(message = "Username is required.")
        String name,

        @Schema(description = "Password", example = "password123")
        @NotEmpty(message = "Password is required.")
        String password) {
}
