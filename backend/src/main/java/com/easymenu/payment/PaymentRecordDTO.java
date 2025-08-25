package com.easymenu.payment;

import com.easymenu.user.UserModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name="PaymentRequest", description = "Necessary data to process a payment request")
public record PaymentRecordDTO(@NotEmpty @Size(min= 2)
                               String transactionId,

                               @NotNull
                               UserModel user,

                               @NotEmpty
                               Long amount,

                               @NotNull
                               PaymentCurrency currency,

                               @NotEmpty
                               String description,

                               @NotNull
                               PaymentProvider provider,

                               @Email @NotEmpty
                               String userEmail,

                               @NotNull
                               PaymentStatus paymentStatus) {}
