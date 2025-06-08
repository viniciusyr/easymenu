package com.easymenu.user.exceptions;

import com.easymenu.infra.exception.GlobalException;
import org.zalando.problem.Status;

public abstract class UserException extends GlobalException {
    protected UserException(String message, String type, String title, Status status) {
        super(message, type, title, status);
    }

    public static class EmailAlreadyExistsException extends UserException {
        public EmailAlreadyExistsException(String message) {
            super(message, "email-already-exists", "Email already exists", Status.BAD_REQUEST);
        }
    }

    public static class UsernameAlreadyExistsException extends UserException {
        public UsernameAlreadyExistsException(String message) {
            super(message, "username-already-exists", "Username already exists", Status.BAD_REQUEST);
        }
    }

    public static class UserNotFoundException extends UserException {
        public UserNotFoundException(String message) {
            super(message, "user-not-found", "User not found", Status.BAD_REQUEST);
        }
    }
}
