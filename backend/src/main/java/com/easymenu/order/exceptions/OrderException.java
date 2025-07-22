package com.easymenu.order.exceptions;

import com.easymenu.infra.exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.zalando.problem.Status;

public class OrderException extends GlobalException {

    public OrderException(String message) {
        super(message, "order-error", "Order error", HttpStatus.BAD_REQUEST);
    }

    public OrderException(String message, String type, String title, HttpStatus status) {
        super(message, type, title, status);
    }

    public static class OrderNotFoundException extends OrderException {
        public OrderNotFoundException(String message) {
            super(message, "order-not-found", "Order not found", HttpStatus.NOT_FOUND);
        }
    }

    public static class ProductNotFoundException extends OrderException {
        public ProductNotFoundException(String message) {
            super(message, "order-product-not-found", "Product in order not found", HttpStatus.NOT_FOUND);
        }
    }

    public static class UserNotFoundException extends OrderException {
        public UserNotFoundException(String message) {
            super(message, "order-user-not-found", "User related to order not found", HttpStatus.NOT_FOUND);
        }
    }

    public static class UpdateNotFoundException extends OrderException {
        public UpdateNotFoundException(String message) {
            super(message, "order-update-not-found", "Order update not found", HttpStatus.NOT_FOUND);
        }
    }

    public static class FilterNotFoundException extends OrderException {
        public FilterNotFoundException(String message) {
            super(message, "search-filter-not-found", "Search filter not found", HttpStatus.NOT_FOUND);
        }
    }
}
