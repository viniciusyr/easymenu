package com.easymenu.user;

import jakarta.validation.constraints.Email;

import java.time.Instant;

public record UserUpdateDto(String name, @Email String email, String password, Instant createdOn, Instant updatedOn) {
}
