package com.easymenu.controllers;

import com.easymenu.dtos.UserRecordDto;
import com.easymenu.models.UserModel;
import com.easymenu.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<UserModel> addUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> usersList = userRepository.findAll();
        if (!usersList.isEmpty()) {
            for (UserModel user : usersList) {
                UUID id = user.getId();
                user.add(linkTo(methodOn(UserController.class).getOneUser(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value="id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        user.get().add(linkTo(methodOn(UserController.class).getOneUser(id)).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserRecordDto userRecordDto, @PathVariable(value="id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        var userModelUpdated = user.get();
        BeanUtils.copyProperties(userRecordDto, userModelUpdated);
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModelUpdated));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value="id") UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted!");
    }

}
