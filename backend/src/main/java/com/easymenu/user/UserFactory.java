package com.easymenu.user;

import com.easymenu.user.enums.UserStatus;
import com.easymenu.user.exceptions.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class UserFactory {

    public UserModel createUser(UserRecordDto userRecordDto) {
        if(userRecordDto == null){
            throw new UserException("UserRecordDto is null");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userRecordDto.password());

        return new UserModel(userRecordDto.name(), userRecordDto.email(), encryptedPassword, UserStatus.ACTIVE, userRecordDto.role());
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
                user.getStatus(),
                user.getRole()
        );
    }

}
