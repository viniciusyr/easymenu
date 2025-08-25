package com.easymenu.payment;

import com.easymenu.user.UserModel;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentFactory {

    public PaymentModel createPayment(String transactionId,
                                      UserModel user,
                                      Long amount,
                                      String currency,
                                      String description,
                                      PaymentProvider provider,
                                      String userEmail,
                                      String paymentStatus,
                                      Instant createdAt){

        return new PaymentModel(transactionId,
                user,
                amount,
                currency,
                description,
                provider,
                userEmail,
                paymentStatus,
                createdAt);

    }

    public PaymentResponseDTO toResponseDTO( PaymentModel paymentModel){
        return new PaymentResponseDTO(paymentModel.id,
                paymentModel.getTransactionId(),
                paymentModel.getUser(),
                paymentModel.getAmount(),
                paymentModel.getCurrency().name(),
                paymentModel.getDescription(),
                paymentModel.getProvider(),
                paymentModel.getUserEmail(),
                paymentModel.getPaymentStatus().name(),
                paymentModel.getCreatedAt());
    }
}
