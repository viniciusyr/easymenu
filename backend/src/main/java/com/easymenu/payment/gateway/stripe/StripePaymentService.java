package com.easymenu.payment.gateway.stripe;

import com.easymenu.payment.*;
import com.easymenu.user.UserModel;
import com.easymenu.user.UserService;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service("stripe")
public class StripePaymentService implements PaymentService {

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    private final UserService userService;
    private final PaymentFactory paymentFactory;
    private final PaymentRepository paymentRepository;

    public StripePaymentService(UserService userService, PaymentFactory paymentFactory, PaymentRepository paymentRepository) {
        this.userService = userService;
        this.paymentFactory = paymentFactory;
        this.paymentRepository = paymentRepository;
    }

    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }

    @Override
    public PaymentResponseDTO processPayment(PaymentRecordDTO paymentDto, UserDetails userDetails) {
        PaymentIntentCreateParams params = PaymentIntentCreateParams
                .builder()
                .setAmount(paymentDto.amount())
                .setCurrency(paymentDto.currency().name())
                .setCustomer(paymentDto.userEmail())
                .setDescription(paymentDto.description())
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            UserModel user = userService.findModelByUsername(userDetails.getUsername());
            PaymentModel newPayment = paymentFactory.createPayment(paymentIntent.getId(),
                    user,
                    paymentIntent.getAmount(),
                    paymentIntent.getCurrency(),
                    paymentIntent.getDescription(),
                    PaymentProvider.STRIPE,
                    paymentIntent.getReceiptEmail(),
                    paymentIntent.getStatus(),
                    Instant.ofEpochSecond(paymentIntent.getCreated()));

            paymentRepository.save(newPayment);
            return paymentFactory.toResponseDTO(newPayment);

        } catch (CardException e){
            throw new StripePaymentException(
                    e.getStripeError().getMessage(),
                    "stripe-card-error",
                    "Stripe Card Error",
                    HttpStatus.BAD_REQUEST,
                    Map.of("type", e.getStripeError().getType(),
                            "decline_code", e.getDeclineCode(),
                            "param", e.getParam())
            );
        } catch (RateLimitException e) {
            throw new StripePaymentException(
                    e.getStripeError().getMessage(),
                    "rate_limit",
                    "Too many requests to Stripe",
                    HttpStatus.BAD_REQUEST,
                    Map.of("type", e.getClass().getSimpleName())
            );
        } catch (StripeException e) {
            throw new StripePaymentException(
                    e.getStripeError().getMessage(),
                    "stripe-generic-error",
                    "Stripe Generic Error",
                    HttpStatus.BAD_REQUEST,
                    Map.of("type", e.getClass().getSimpleName())
            );
        }
    }
}
