package com.easymenu.dtos;

import com.easymenu.enums.UserStatus;
import jakarta.validation.constraints.*;

import java.time.Instant;


public record UserRecordDto(@NotEmpty @Size(min = 3, max = 100) String name,
                            @NotEmpty @Email String email,
                            @NotEmpty String password,
                             Instant createdOn,
                             Instant updatedOn) {

}
