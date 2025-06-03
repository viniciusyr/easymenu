package com.easymenu.user;

import com.easymenu.user.enums.UserStatus;
import com.easymenu.user.exceptions.UserException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserFactory userFactory;
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
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

        String encodedPassword = passwordEncoder.encode(userRecordDto.password());

        UserModel newUser =  userFactory.createUser(
                new UserRecordDto(
                        userRecordDto.name(),
                        userRecordDto.email(),
                        encodedPassword,
                        userRecordDto.role()
                )
        );

        userRepository.save(newUser);

        log.info("User created successfully: {}", newUser.getEmail());

        return userFactory.toResponseDto(newUser);
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
    public void inactiveUser(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new UserException.UserNotFoundException("Please provide a valid id");
        }

        UserModel user = userRepository.findById(id)
                .orElseThrow(() ->  new UserException.UserNotFoundException("User wasn't found to change status"));

        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        log.info("User deleted successfully");

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
    public UserDetails findByName(String name) {
        UserModel user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + name));

        return user;
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userFactory::toResponseDto).toList();
    }

    @Override
    public List<UserResponseDto> getUsersByStatus(UserStatus status) {
        return userRepository.findAllByStatus(status).stream()
                .map(userFactory::toResponseDto).toList();
    }
}