package com.easymenu.product.exceptions;

import com.easymenu.infra.exception.GlobalException;
import org.zalando.problem.Status;

public class ProductException extends GlobalException {
    public ProductException(String message) {
        super(message, "product-error", "Product error", Status.BAD_REQUEST);
    }

    public static class ProductNotFoundException extends GlobalException {
        public ProductNotFoundException(String message) {
            super(message, "product-not-found", "Product not found", Status.NOT_FOUND);
        }
    }

    public static class NameAlreadyExistsException extends GlobalException{
        public NameAlreadyExistsException(String message){
            super(message, "product-name-already-exists", "Product name already exists", Status.CONFLICT);
        }
    }

}
