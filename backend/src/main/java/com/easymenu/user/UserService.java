package com.easymenu.user;

import com.easymenu.user.enums.UserStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserRecordDto userRecordDto);
    UserResponseDto updateUser(UserUpdateDto userUpdateDto, UUID id);
    void inactiveUser(UUID id);
    UserResponseDto getOneUser(UUID id);
    UserDetails findByName(String name);
    List<UserResponseDto> getUsers();
    List<UserResponseDto> getUsersByStatus(UserStatus status);

}
