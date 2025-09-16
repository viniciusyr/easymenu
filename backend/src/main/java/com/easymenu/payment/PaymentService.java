package com.easymenu.payment;

import org.springframework.security.core.userdetails.UserDetails;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRecordDTO paymentDto, UserDetails user);

}
