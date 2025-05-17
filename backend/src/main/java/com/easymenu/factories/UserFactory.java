package com.easymenu.factories;

import com.easymenu.dtos.UserRecordDto;
import com.easymenu.dtos.UserResponseDto;
import com.easymenu.dtos.UserUpdateDto;
import com.easymenu.enums.UserStatus;
import com.easymenu.exceptions.UserException;
import com.easymenu.models.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class UserFactory {

    public UserModel createUser(UserRecordDto userRecordDto) {
        if(userRecordDto == null){
            throw new UserException("UserRecordDto is null");
        }

        // THE PASSWORD MUST BE SCRIPTED LATER USING SPRING SECURITY @PASSWORD ENCODER

        return new UserModel(userRecordDto.name(), userRecordDto.email(), userRecordDto.password(), UserStatus.ACTIVE);
    }

    public void applyUpdates(UserUpdateDto updates, UserModel existingUser) {
        if (updates.name() != null) {
            existingUser.setName(updates.name());
        }
        if (updates.email() != null) {
            existingUser.setEmail(updates.email());
        }
        if (updates.password() != null) {
            existingUser.setPassword(updates.password());
        }
        existingUser.setUpdatedOn(Instant.now());
    }

    public UserResponseDto toResponseDto(UserModel user) {

        if(user.getId() == null){
            throw new UserException.UserToResponseException("User ID is null");
        }

        return new UserResponseDto(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedOn(),
                user.getUpdatedOn(),
                user.getStatus());
    }

}
