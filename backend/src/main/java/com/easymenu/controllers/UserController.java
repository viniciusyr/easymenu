package com.easymenu.controllers;

import com.easymenu.dtos.UserRecordDto;
import com.easymenu.dtos.UserResponseDto;
import com.easymenu.dtos.UserUpdateDto;
import com.easymenu.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserRecordDto user) {
        UserResponseDto newUser = userService.createUser(user);
        newUser.add(linkTo(methodOn(UserController.class).getOneUser(newUser.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> usersList = userService.getUsers();
        if (usersList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        usersList.forEach(user ->
                user.add(linkTo(methodOn(UserController.class)
                        .getOneUser(user.getId())).withSelfRel()));

        return ResponseEntity.ok(usersList);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getOneUser(@PathVariable(value="id") UUID id) {
        UserResponseDto user = userService.getOneUser(id);
        user.add(linkTo(methodOn(UserController.class).getOneUser(id)).withSelfRel());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody @Valid UserUpdateDto userUpdatedDto, @PathVariable(value="id") UUID id) {
        UserResponseDto updatedUser = userService.updateUser(userUpdatedDto, id);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<Object> inactiveUserStatus(@PathVariable(value="id") UUID id) {
        userService.inactiveUser(id);
        return ResponseEntity.ok("User's status was successfully changed");
    }

}
