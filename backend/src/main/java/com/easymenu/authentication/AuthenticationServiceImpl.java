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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sound.midi.SysexMessage;

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

        Authentication auth;

        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationDTO.name(), authenticationDTO.password())
            );
        } catch (BadCredentialsException e) {
            throw new AuthenticationException.InvalidPasswordException("Username or password incorrect");
        } catch (LockedException e) {
            throw new AuthenticationException.InactiveUserException("Account is locked");
        }

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        log.info("User {} logged successfully", authenticationDTO.name());
        
        return new LoginResponseDTO(token);
    }

    @Override
    public UserResponseDTO register(UserRecordDTO userRecordDTO) {
        return userService.createUser(userRecordDTO);
    }

}
