package com.easymenu.authentication.exceptions;

import org.springframework.http.HttpStatus;

public class ResponseStatusException extends RuntimeException {
    public ResponseStatusException(HttpStatus unauthorized, String message) {
        super(message);
    }
}
