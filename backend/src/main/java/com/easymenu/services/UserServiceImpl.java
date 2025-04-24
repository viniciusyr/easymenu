package com.easymenu.services;

import com.easymenu.dtos.UserRecordDto;
import com.easymenu.dtos.UserResponseDto;
import com.easymenu.dtos.UserUpdateDto;
import com.easymenu.exceptions.UserException;
import com.easymenu.factories.UserFactory;
import com.easymenu.models.UserModel;
import com.easymenu.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserFactory userFactory;
    private final UserRepository userRepository;

    public UserServiceImpl(UserFactory userFactory, UserRepository userRepository) {
        this.userFactory = userFactory;
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto createUser(UserRecordDto userRecordDto) {
        if (userRepository.existsByEmail(userRecordDto.email())) {
            throw new UserException.EmailAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByName(userRecordDto.name())) {
            throw new UserException.UsernameAlreadyExistsException("Username already exists");
        }

        UserModel savedUser = userFactory.createUser(userRecordDto);

        userRepository.save(savedUser);

        log.info("User created successfully: {}", savedUser.getEmail());

        return userFactory.toResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(UserUpdateDto userUpdateDto, UUID id) {

        UserModel existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found: " + id));

        if (userUpdateDto.name() != null && !userUpdateDto.name().isBlank()) {
            if (userRepository.existsByName(userUpdateDto.name()) &&
                    !userUpdateDto.name().equals(existingUser.getName())) {
                throw new UserException.UsernameAlreadyExistsException("Username already exists");
            }
        }

        if (userUpdateDto.email() != null && !userUpdateDto.email().isBlank()) {
            if (userRepository.existsByEmail(userUpdateDto.email()) &&
                    !userUpdateDto.email().equals(existingUser.getEmail())) {
                throw new UserException.EmailAlreadyExistsException("Email already exists");
            }
        }

        userFactory.applyUpdates(userUpdateDto, existingUser);

        UserModel savedUser = userRepository.save(existingUser);

        MDC.put("userId", savedUser.getId().toString());
        log.info("User updated successfully: {}", savedUser.getEmail());
        MDC.clear();

        return userFactory.toResponseDto(savedUser);
    }

    @Override
    public void deleteUser(UUID id) {

        if (id == null) {
            throw new UserException.UserNotFoundException("Please provide a valid id");
        }

        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found: " + id));

        userRepository.deleteById(id);

        log.info("User deleted successfully: {}", user.getEmail());

    }

    @Override
    public UserResponseDto getOneUser(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    log.info("User found: {}", user.getEmail());
                    return userFactory.toResponseDto(user);
                })
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found: " + id));

    }

    @Override
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userFactory::toResponseDto).toList();
    }
}