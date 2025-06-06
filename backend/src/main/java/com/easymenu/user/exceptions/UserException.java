package com.easymenu.user.exceptions;

import com.easymenu.infra.exception.GlobalException;
import org.zalando.problem.Status;

public class UserException extends GlobalException {

    public UserException(String message) {
        super(message, "user-error", "User error", Status.BAD_REQUEST);
    }

    public static class EmailAlreadyExistsException extends GlobalException {
        public EmailAlreadyExistsException(String message) {
            super(message, "email-already-exists", "Email already exists", Status.BAD_REQUEST);
        }
    }

    public static class UsernameAlreadyExistsException extends GlobalException {
        public UsernameAlreadyExistsException(String message) {
            super(message, "username-already-exists", "Username already exists", Status.BAD_REQUEST);
        }
    }

    public static class UserNotFoundException extends GlobalException {
        public UserNotFoundException(String message) {
            super(message, "user-not-found", "User not found", Status.BAD_REQUEST);
        }
    }

}
