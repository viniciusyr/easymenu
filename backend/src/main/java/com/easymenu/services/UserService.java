package com.easymenu.services;

import com.easymenu.dtos.UserRecordDto;
import com.easymenu.dtos.UserResponseDto;
import com.easymenu.dtos.UserUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserRecordDto userRecordDto);
    UserResponseDto updateUser(UserUpdateDto userUpdateDto, UUID id);
    void deleteUser(UUID id);
    UserResponseDto getOneUser(UUID id);
    List<UserResponseDto> getUsers();

}
