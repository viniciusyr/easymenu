package com.easymenu.user;

import com.easymenu.user.enums.UserRole;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserRequest", description = "Data to register a new user")
public record UserRecordDTO(

        @Schema(description = "Full name of the user", example = "John Blue ")
        @NotEmpty @Size(min = 2, max = 100)
        String name,

        @Schema(description = "Valid email address", example = "john.blue@example.com")
        @NotEmpty @Email
        String email,

        @Schema(description = "User password (must be encrypted before storage)", example = "12345678")
        @NotEmpty
        String password,

        @Schema(description = "User role: ADMIN or USER", example = "USER")
        UserRole role
) {}

