package com.easymenu.authentication;

import com.easymenu.user.UserController;
import com.easymenu.user.UserRecordDTO;
import com.easymenu.user.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@Tag(name="Authentication")
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Login Authentication", description = "User login")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User credentials",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = AuthenticationRecordDTO.class),
                    examples = @ExampleObject(value = """
            {
              "name": "username",
              "password": "password"
            }
            """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Login success."),
            @ApiResponse(responseCode = "403", description = "Access denied.")
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationRecordDTO authenticationDto) {
        LoginResponseDTO loginResponseDTO = authenticationService.login(authenticationDto);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/register")
    @Operation(summary = "User Registration ", description = "Create a new product")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UserRecordDTO.class),
                    examples = @ExampleObject(value = """
            {
              "name": "username",
              "email": "user@email.com",
              "password": "userpassword"
            }
            """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created. "),
            @ApiResponse(responseCode = "403", description = "Access denied."),
    })
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRecordDTO registerDto) {
        UserResponseDTO user = authenticationService.register(registerDto);
        user.add(linkTo(methodOn(UserController.class).getOneUser(user.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
