package com.easymenu.product.exceptions;

public class ProductException extends RuntimeException {
    public ProductException(String message) {
        super(message);
    }

    public static class ProductNotFoundException extends ProductException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidProductException extends ProductException {
        public InvalidProductException(String message) {
            super(message);
        }
    }

    public static class InvalidProductRecordException extends ProductException {
        public InvalidProductRecordException(String message) {
            super(message);
        }
    }

    public static class InvalidProductUpdateException extends ProductException {
        public InvalidProductUpdateException(String message) {
            super(message);
        }
    }

    public static class NameAlreadyExistsException extends ProductException {
        public NameAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class BatchAlreadyExistsException extends ProductException {
        public BatchAlreadyExistsException(String message) {
            super(message);
        }
    }

}
