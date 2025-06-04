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

    public UserModel createUser(UserRecordDTO userRecordDto) {
        if(userRecordDto == null){
            throw new UserException("UserRecordDTO is null");
        }

        return new UserModel(userRecordDto.name(),
                userRecordDto.email(),
                userRecordDto.password(),
                UserStatus.ACTIVE,
                userRecordDto.role());
    }

    public void applyUpdates(UserUpdateDTO updates, UserModel existingUser) {
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

    public UserResponseDTO toResponseDto(UserModel user) {
        return new UserResponseDTO(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedOn(),
                user.getUpdatedOn(),
                user.getStatus(),
                user.getRole()
        );
    }

}
