package com.easymenu.authentication;

import com.easymenu.product.ProductController;
import com.easymenu.user.UserController;
import com.easymenu.user.UserRecordDTO;
import com.easymenu.user.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationRecordDTO authenticationDto) {
        LoginResponseDTO loginResponseDTO = authenticationService.login(authenticationDto);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRecordDTO registerDto) {
        UserResponseDTO user = authenticationService.register(registerDto);
        user.add(linkTo(methodOn(UserController.class).getOneUser(user.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
