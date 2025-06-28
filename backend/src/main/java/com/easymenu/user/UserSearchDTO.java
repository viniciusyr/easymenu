package com.easymenu.user;

import com.easymenu.user.enums.UserRole;
import com.easymenu.user.enums.UserStatus;

import java.time.LocalDate;
import java.util.UUID;

public record UserSearchDTO(
        UUID id,
        String name,
        String email,
        UserStatus status,
        UserRole role,
        LocalDate startDate,
        LocalDate endDate
) {
}
