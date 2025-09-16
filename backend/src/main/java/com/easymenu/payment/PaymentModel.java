package com.easymenu.payment;

import com.easymenu.user.UserModel;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="TB_PAYMENTS")
@Entity
public class PaymentModel {
    @Id
    @GeneratedValue()
    UUID id;
    String transactionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserModel user;
    Long amount;
    PaymentCurrency currency;
    String description;
    PaymentProvider provider;
    String userEmail;
    PaymentStatus paymentStatus;

    @Timestamp
    Instant createdAt;

    public PaymentModel(String transactionId, UserModel user, Long amount, String currency, String description, PaymentProvider provider, String userEmail, String paymentStatus, Instant createdAt) {

    }
}
