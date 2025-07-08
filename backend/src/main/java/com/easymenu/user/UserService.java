package com.easymenu.user;

import com.easymenu.user.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDTO createUser(UserRecordDTO userRecordDto);
    UserResponseDTO updateUser(UserUpdateDTO userUpdateDto, UUID id);
    void inactiveUser(UUID id);
    void activeUser(UUID id);
    UserResponseDTO getOneUser(UUID id);
    UserDetails findByName(String name);
    List<UserResponseDTO> getUsers();
    Page<UserResponseDTO> findByCriteria(UserSearchDTO userSearchDTO, Pageable pageable);

}
