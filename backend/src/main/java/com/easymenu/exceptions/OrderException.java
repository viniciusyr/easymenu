package com.easymenu.exceptions;

public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }

    public static class OrderNotFoundException extends OrderException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }

    public static class ProductNotFoundException extends OrderException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends OrderException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class UpdateNotFoundException extends OrderException{
        public UpdateNotFoundException(String message){ super(message); }
    }
}
