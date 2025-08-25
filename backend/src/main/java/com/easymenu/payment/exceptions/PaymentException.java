package com.easymenu.payment.exceptions;

import com.easymenu.infra.exception.GlobalException;
import com.easymenu.payment.PaymentProvider;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class PaymentException extends GlobalException {
    private final PaymentProvider provider;
    private final Map<String, Object> details;

    public PaymentException(String message, String type, String title, HttpStatus status, PaymentProvider provider, Map<String, Object> errorDetails) {
        super(message, type, title, status);
        this.provider = provider;
        this.details = new LinkedHashMap<>(errorDetails);
    }
}
