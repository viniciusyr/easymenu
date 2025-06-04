package com.easymenu.authentication;

import com.easymenu.authentication.exceptions.ResponseStatusException;
import com.easymenu.user.UserRecordDTO;
import com.easymenu.user.UserResponseDTO;
import com.easymenu.user.exceptions.UserException;
import com.easymenu.infra.security.TokenService;
import com.easymenu.user.UserModel;
import com.easymenu.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRecordDTO authenticationDto) {
        try {
            var userPassword = new UsernamePasswordAuthenticationToken(authenticationDto.name(), authenticationDto.password());
            var auth = this.authenticationManager.authenticate(userPassword);
            var token = tokenService.generateToken((UserModel) auth.getPrincipal());
            log.info("Password received: {}", authenticationDto.password());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRecordDTO registerDto) {
        try {
            UserResponseDTO createdUser = userService.createUser(registerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (UserException.EmailAlreadyExistsException | UserException.UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
