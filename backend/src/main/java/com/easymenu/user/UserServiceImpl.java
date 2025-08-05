package com.easymenu.user;

import com.easymenu.redis.RedisService;
import com.easymenu.user.enums.UserStatus;
import com.easymenu.user.exceptions.UserException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserFactory userFactory;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final String USER_CACHE_KEY = "USER_CACHE::";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserFactory userFactory, UserRepository userRepository, RedisService redisService) {
        this.userFactory = userFactory;
        this.userRepository = userRepository;
        this.redisService = redisService;
    }

    @Override
    @CachePut(value = "USER_CACHE", key = "#result.id")
    public UserResponseDTO createUser(UserRecordDTO userRecordDto) {
        if (userRepository.existsByEmail(userRecordDto.email())) {
            throw new UserException.EmailAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByName(userRecordDto.name())) {
            throw new UserException.UsernameAlreadyExistsException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(userRecordDto.password());

        UserModel newUser =  userFactory.createUser(
                new UserRecordDTO(
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
    @CachePut(value="USER_CACHE", key = "#result.id")
    public UserResponseDTO updateUser(UserUpdateDTO userUpdateDto, UUID id) {

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
        UserUpdateDTO updates = userUpdateDto;
        if (userUpdateDto.password() != null && !userUpdateDto.password().isBlank()) {
            String encodedPassword = passwordEncoder.encode(userUpdateDto.password());
            updates = new UserUpdateDTO(
                    userUpdateDto.name(),
                    userUpdateDto.email(),
                    encodedPassword,
                    userUpdateDto.createdOn(),
                    userUpdateDto.updatedOn()
            );
        }
        userFactory.applyUpdates(updates, existingUser);

        UserModel savedUser = userRepository.save(existingUser);

        MDC.put("userId", savedUser.getId().toString());
        log.info("User updated successfully: {}", savedUser.getEmail());
        MDC.clear();

        return userFactory.toResponseDto(savedUser);
    }

    @Override
    public void inactiveUser(UUID id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException("Please enter a valid id"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UserException.UserWrongStatusException("The user's status is already INACTIVE");
        }

        user.setStatus(UserStatus.INACTIVE);
        user.setUpdatedOn(Instant.now());
        userRepository.save(user);

        log.info("User {} was successfully deactivated", id);
    }

    @Override
    public void activeUser(UUID id) {

        UserModel user = userRepository.findById(id)
                .orElseThrow(() ->  new UserException.UserNotFoundException("User wasn't found to change status"));

        if(user.getStatus() != UserStatus.INACTIVE){
            throw new UserException.UserWrongStatusException("The user's status is already ACTIVE");
        } else {
            user.setStatus(UserStatus.ACTIVE);
            user.setUpdatedOn(Instant.now());
            userRepository.save(user);

            log.info("User {} was successfully activated", id);
        }
    }

    @Override
    public UserResponseDTO getOneUser(UUID id) {
        String cacheKey = USER_CACHE_KEY + id;
        return redisService.get(cacheKey, UserResponseDTO.class)
                .orElseGet(() -> {
                    UserResponseDTO user = userRepository.findById(id)
                            .map(userFactory::toResponseDto)
                            .orElseThrow(() -> new UserException.UserNotFoundException("User not found by id" + id));
                    redisService.set(cacheKey, user, Duration.ofMinutes(10));
                    return user;
                });
    }

    @Override
    public UserDetails findByName(String name) {
        String cacheKey = USER_CACHE_KEY + name;
        return redisService.get(cacheKey, UserModel.class)
                .orElseGet(() -> {
                    log.info("User found {}", name);
                    UserModel user = userRepository.findByName(name)
                            .orElseThrow(() -> new UserException.UserNotFoundException("User not found by name: "+ name));
                    redisService.set(cacheKey, user, Duration.ofMinutes(10));
                    return user;
                });


    }

    @Override
    public List<UserResponseDTO> getUsers() {
        return redisService.getList(USER_CACHE_KEY, UserResponseDTO.class)
                        .orElseGet(() -> {
                            List<UserResponseDTO> users = userRepository.findAll()
                                    .stream()
                                    .map(userFactory::toResponseDto).
                                    toList();
                            redisService.set(USER_CACHE_KEY, users, Duration.ofMinutes(10));
                            return users;
                        });

    }

    @Override
    public Page<UserResponseDTO> findByCriteria(UserSearchDTO userSearchDTO, Pageable pageable) {
        if(userSearchDTO == null){
            throw new UserException.FilterNotFoundException("searchDTO is null");
        }

        Specification<UserModel> spec = Specification.where(null);

        if(userSearchDTO.id() != null){
            spec = spec.and(UserSpecs.hasId(userSearchDTO.id()));
        }

        if(userSearchDTO.name() != null){
            spec = spec.and(UserSpecs.containsName(userSearchDTO.name()));
        }

        if(userSearchDTO.email() != null){
            spec = spec.and(UserSpecs.containsEmail(userSearchDTO.email()));
        }

        if(userSearchDTO.status() != null){
            spec = spec.and(UserSpecs.hasStatus(userSearchDTO.status()));
        }

        if(userSearchDTO.role() != null){
            spec = spec.and(UserSpecs.hasRole(userSearchDTO.role()));
        }

        if (userSearchDTO.startDate() != null && userSearchDTO.endDate() != null) {
            spec = spec.and(UserSpecs.betweenDates(userSearchDTO.startDate(), userSearchDTO.endDate()));
        } else if (userSearchDTO.startDate() != null) {
            spec = spec.and(UserSpecs.createdAfter(userSearchDTO.startDate()));
        } else if (userSearchDTO.endDate() != null) {
            spec = spec.and(UserSpecs.createdBefore(userSearchDTO.endDate()));
        }

        if (userSearchDTO.updatedOn() != null) {
            spec = spec.and(UserSpecs.updatedOn(userSearchDTO.updatedOn()));
        }

        Page<UserModel> pageResult = userRepository.findAll(spec, pageable);

        List<UserResponseDTO> responseList = pageResult.getContent().stream()
                .map(userFactory::toResponseDto)
                .toList();

        return new PageImpl<>(responseList, pageable, pageResult.getTotalElements());
    }
}