package com.easymenu.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDto extends RepresentationModel<UserResponseDto> {

    private UUID id;
    private String name;
    private String email;
    private Instant createdOn;
    private Instant updatedOn;


    public UserResponseDto(UUID id, String name, String email, Instant createdOn, Instant updatedOn) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }


}