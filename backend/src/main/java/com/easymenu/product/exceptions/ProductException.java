package com.easymenu.product.exceptions;

import com.easymenu.infra.exception.GlobalException;
import org.zalando.problem.Status;

public abstract class ProductException extends GlobalException {

    protected ProductException(String message, String type, String title, Status status) {
        super(message, type, title, status);
    }

    public static class ProductNotFoundException extends ProductException {
        public ProductNotFoundException(String message) {
            super(message, "product-not-found", "Product not found", Status.NOT_FOUND);
        }
    }

    public static class NameAlreadyExistsException extends ProductException {
        public NameAlreadyExistsException(String message) {
            super(message, "product-name-already-exists", "Product name already exists", Status.CONFLICT);
        }
    }

    public static class InvalidPriceException extends ProductException {
        public InvalidPriceException(String message) {
            super(message, "invalid-product-price", "Invalid product price", Status.BAD_REQUEST);
        }
    }

    public static class InvalidBatchException extends ProductException {
        public InvalidBatchException(String message) {
            super(message, "invalid-product-batch", "Invalid product batch", Status.BAD_REQUEST);
        }
    }

    public static class ExpiredProductException extends ProductException {
        public ExpiredProductException(String message) {
            super(message, "product-expired", "Product is expired", Status.BAD_REQUEST);
        }
    }
}
