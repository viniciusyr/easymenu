package com.easymenu.user;

import com.easymenu.user.enums.UserStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDTO createUser(UserRecordDTO userRecordDto);
    UserResponseDTO updateUser(UserUpdateDTO userUpdateDto, UUID id);
    void inactiveUser(UUID id);
    UserResponseDTO getOneUser(UUID id);
    UserDetails findByName(String name);
    List<UserResponseDTO> getUsers();
    List<UserResponseDTO> getUsersByStatus(UserStatus status);

}
