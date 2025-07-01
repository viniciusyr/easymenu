package com.easymenu.user;

import com.easymenu.utils.PageResultDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> usersList = userService.getUsers();
        if (usersList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        usersList.forEach(user ->
                user.add(linkTo(methodOn(UserController.class)
                        .getOneUser(user.getId())).withSelfRel()));

        return ResponseEntity.ok(usersList);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getOneUser(@PathVariable(value="id") UUID id) {
        UserResponseDTO user = userService.getOneUser(id);
        user.add(linkTo(methodOn(UserController.class).getOneUser(id)).withSelfRel());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserUpdateDTO userUpdatedDto, @PathVariable(value="id") UUID id) {
        UserResponseDTO updatedUser = userService.updateUser(userUpdatedDto, id);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<Object> inactiveUserStatus(@PathVariable(value="id") UUID id) {
        userService.inactiveUser(id);
        return ResponseEntity.ok("User's status was successfully changed");
    }

    @GetMapping("/users/search")
    public ResponseEntity<PageResultDTO<UserResponseDTO>> getUserByCriteria(@RequestBody UserSearchDTO userSearchDTO, Pageable pageable){
        Page<UserResponseDTO> users = userService.findByCriteria(userSearchDTO, pageable);
        users.forEach(user ->
            user.add(linkTo(methodOn(UserController.class).getOneUser(user.getId())).withSelfRel()));
        return ResponseEntity.ok(PageResultDTO.result(users));
    }

}
