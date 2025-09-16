package com.easymenu.payment;


import com.easymenu.user.UserModel;

import java.time.Instant;
import java.util.UUID;

public record PaymentResponseDTO(
        UUID id,
        String transactionId,
        UserModel user,
        Long amount,
        String currency,
        String description,
        PaymentProvider provider,
        String userEmail,
        String paymentStatus,
        Instant createdAt) {}
