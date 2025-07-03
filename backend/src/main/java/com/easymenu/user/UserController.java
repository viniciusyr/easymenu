package com.easymenu.user;

import com.easymenu.utils.PageResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Operations related to system users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "List all users", description = "Returns all registered users with self-links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "204", description = "No users registered")
    })
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

    @Operation(summary = "Get user by ID", description = "Returns a single user by their ID")
    @Parameter(name = "id", description = "User ID", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getOneUser(@PathVariable(value="id") UUID id) {
        UserResponseDTO user = userService.getOneUser(id);
        user.add(linkTo(methodOn(UserController.class).getOneUser(id)).withSelfRel());
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user", description = "Updates a user's data")
    @Parameters({
            @Parameter(name = "id", description = "User ID to update", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "New user data",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UserUpdateDTO.class),
                    examples = @ExampleObject(value = """
                        {
                          "name": "Updated Name",
                          "email": "updated@email.com"
                        }
                    """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserUpdateDTO userUpdatedDto,
                                                      @PathVariable(value="id") UUID id) {
        UserResponseDTO updatedUser = userService.updateUser(userUpdatedDto, id);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Change user status", description = "Deactivates or reactivates a user by ID")
    @Parameter(name = "id", description = "User ID", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status successfully changed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/users/{id}/status")
    public ResponseEntity<Object> inactiveUserStatus(@PathVariable(value="id") UUID id) {
        userService.inactiveUser(id);
        return ResponseEntity.ok("User's status was successfully changed");
    }

    @Operation(summary = "Search users by criteria", description = "Search users with pagination and optional filters")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User search criteria",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UserSearchDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                      "name": "Example",
                      "email": "example@email.com",
                      "status": "ACTIVE",
                      "role": "USER / ADMIN"
                    }
                    """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users matching criteria returned")
    })
    @GetMapping("/users/search")
    public ResponseEntity<PageResultDTO<UserResponseDTO>> getUserByCriteria(@RequestBody UserSearchDTO userSearchDTO,
                                                                            Pageable pageable){
        Page<UserResponseDTO> users = userService.findByCriteria(userSearchDTO, pageable);
        users.forEach(user ->
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getId())).withSelfRel()));
        return ResponseEntity.ok(PageResultDTO.result(users));
    }
}
