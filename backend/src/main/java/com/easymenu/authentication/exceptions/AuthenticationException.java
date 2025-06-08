package com.easymenu.authentication.exceptions;

import com.easymenu.infra.exception.GlobalException;
import org.zalando.problem.Status;

public class AuthenticationException extends GlobalException {

    public AuthenticationException(String message) {
        super(message, "authentication-error", "Authentication error", Status.BAD_REQUEST);
    }

    public static class InactiveUserException extends GlobalException{
        public InactiveUserException(String message){
            super(message, "inactive-user-error", "Inactive user error", Status.BAD_REQUEST);
        }
    }

    public static class InvalidPasswordException extends GlobalException {
        public InvalidPasswordException(String message) {
            super(message, "invalid-credentials-error", "Invalid user credentials", Status.BAD_REQUEST);
        }
    }
}
