package com.easymenu.authentication.exceptions;

import com.easymenu.infra.exception.GlobalException;
import org.zalando.problem.Status;

public class AuthenticationException extends GlobalException {

    public AuthenticationException(String message) {
        super(message, "authentication-error", "Authentication error", Status.BAD_REQUEST);
    }

    public AuthenticationException(String message, String type, String title, Status status) {
        super(message, type, title, status);
    }

    public static class InactiveUserException extends AuthenticationException {
        public InactiveUserException(String message) {
            super(message, "inactive-user-error", "Inactive user error", Status.BAD_REQUEST);
        }
    }

    public static class InvalidPasswordException extends AuthenticationException {
        public InvalidPasswordException(String message) {
            super(message, "invalid-credentials-error", "Invalid user credentials", Status.BAD_REQUEST);
        }
    }

    public static class InvalidTokenException extends AuthenticationException {
        public InvalidTokenException(String message) {
            super(message, "invalid-token", "Invalid token", Status.UNAUTHORIZED);
        }
    }
}

