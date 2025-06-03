package com.easymenu.user;

import com.easymenu.user.enums.UserRole;
import com.easymenu.user.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> {

    private UUID id;
    private String name;
    private String email;
    private UserStatus status;
    private UserRole role;
    private Instant createdOn;
    private Instant updatedOn;



    public UserResponseDTO(UUID id, String name, String email, Instant createdOn, Instant updatedOn, UserStatus status, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.status = status;
        this.role = role;
    }


    public UserResponseDTO(UUID id, String name, String email, UserStatus status, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.role = role;
    }

}