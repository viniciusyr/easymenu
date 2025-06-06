package com.easymenu.authentication;

import com.easymenu.user.UserRecordDTO;
import com.easymenu.user.UserResponseDTO;

public interface AuthenticationService {
    LoginResponseDTO login(AuthenticationRecordDTO authenticationDTO);
    UserResponseDTO register(UserRecordDTO userRecordDTO);
}
