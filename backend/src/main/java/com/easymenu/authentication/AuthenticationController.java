package com.easymenu.authentication;

import com.easymenu.user.UserRecordDto;
import com.easymenu.user.UserResponseDto;
import com.easymenu.user.exceptions.UserException;
import com.easymenu.infra.security.TokenService;
import com.easymenu.user.UserModel;
import com.easymenu.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRecordDto authenticationDto){
        var userPassword = new UsernamePasswordAuthenticationToken(authenticationDto.name(), authenticationDto.password());
        var auth = this.authenticationManager.authenticate(userPassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRecordDto registerDto) {
        try {
            UserResponseDto createdUser = userService.createUser(registerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (UserException.EmailAlreadyExistsException | UserException.UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
