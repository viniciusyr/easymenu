package com.easymenu.authentication;

import com.easymenu.authentication.exceptions.AuthenticationException;
import com.easymenu.infra.security.TokenService;
import com.easymenu.user.UserModel;
import com.easymenu.user.UserRecordDTO;
import com.easymenu.user.UserResponseDTO;
import com.easymenu.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthenticationServiceImpl(TokenService tokenService, AuthenticationManager authenticationManager, UserService userService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public LoginResponseDTO login(AuthenticationRecordDTO authenticationDTO) {
        UserDetails userDetails = userService.findByName(authenticationDTO.name());

        if(!passwordMatches(authenticationDTO.password(), userDetails.getPassword())){
            throw new AuthenticationException.InvalidPasswordException("Username or password incorrect");
        }

        if(!userDetails.isAccountNonLocked()){
                throw new AuthenticationException.InactiveUserException("User account status is currently inactive");
        }

        var userPassword = new UsernamePasswordAuthenticationToken(authenticationDTO.name(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(userPassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        log.info("User {} logged successfully", authenticationDTO.name());

        return new LoginResponseDTO(token);
    }

    @Override
    public UserResponseDTO register(UserRecordDTO userRecordDTO) {
        return userService.createUser(userRecordDTO);
    }

    private boolean passwordMatches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
