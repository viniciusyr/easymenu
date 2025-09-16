package com.easymenu.payment.gateway.stripe;

import com.easymenu.payment.PaymentProvider;
import com.easymenu.payment.exceptions.PaymentException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class StripePaymentException extends PaymentException {
    public StripePaymentException(String message, String type, String title, HttpStatus status, Map<String, Object> errorDetails) {
        super(message, type, title, status, PaymentProvider.STRIPE, errorDetails);
    }
}
