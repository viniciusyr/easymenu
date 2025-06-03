package com.easymenu.user;

import com.easymenu.user.enums.UserRole;
import jakarta.validation.constraints.*;


public record UserRecordDTO(
        @NotEmpty @Size(min = 2, max = 100) String name,
        @NotEmpty @Email String email,
        @NotEmpty String password,
        UserRole role
) {}
