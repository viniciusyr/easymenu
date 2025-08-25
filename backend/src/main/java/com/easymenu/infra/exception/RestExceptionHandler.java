package com.easymenu.infra.exception;

import com.easymenu.authentication.exceptions.AuthenticationException;
import com.easymenu.payment.exceptions.PaymentException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.violations.Violation;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HandlerFactory> handleValidation(MethodArgumentNotValidException ex) {
        List<Violation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        HandlerFactory problem = HandlerFactory.builder()
                .withType(URI.create("https://easymenu.app/problems/validation-error"))
                .withTitle("Validation Error")
                .withStatus(HttpStatus.BAD_REQUEST)
                .withDetail("One or more fields are invalid.")
                .withTime(Instant.now())
                .with("violations", violations)
                .build();

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<HandlerFactory> handleGlobal(GlobalException ex) {
        HandlerFactory problem = HandlerFactory.builder()
                .withType(URI.create("https://easymenu.app/problems/" + ex.getType()))
                .withTitle(ex.getTitle())
                .withStatus(ex.getStatus())
                .withDetail(ex.getMessage())
                .withTime(Instant.now())
                .build();

        return ResponseEntity.status(ex.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HandlerFactory> handleNotReadable(HttpMessageNotReadableException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException invalidFormat) {
            String field = extractField(invalidFormat.getPath());
            String value = String.valueOf(invalidFormat.getValue());

            HandlerFactory problem = HandlerFactory.builder()
                    .withType(URI.create("https://easymenu.app/problems/invalid-format"))
                    .withTitle("Invalid Format")
                    .withStatus(HttpStatus.BAD_REQUEST)
                    .withDetail("Wrong type value for the field: " + field)
                    .withTime(Instant.now())
                    .with("Invalid value", value)
                    .build();

            return ResponseEntity.badRequest().body(problem);
        }

        HandlerFactory genericProblem = HandlerFactory.builder()
                .withType(URI.create("https://easymenu.app/problems/json-error"))
                .withTitle("Error JSON")
                .withStatus(HttpStatus.BAD_REQUEST)
                .withDetail("The submitted request could not be processed")
                .withTime(Instant.now())
                .build();

        return ResponseEntity.badRequest().body(genericProblem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HandlerFactory> handleGeneric(Exception ex) {
        HandlerFactory problem = HandlerFactory.builder()
                .withType(URI.create("https://easymenu.app/problems/internal-error"))
                .withTitle("Internal Error")
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withDetail("An unexpected error occurred.")
                .withTime(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problem);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<HandlerFactory> paymentHandler(PaymentException ex){

        HandlerFactory problem = HandlerFactory.builder()
                .withType(URI.create("https://easymenu.app/problems/payment-request-error"))
                .withTitle(ex.getTitle())
                .withStatus(ex.getStatus())
                .withDetail(ex.getMessage())
                .withTime(Instant.now())
                .with("provider", ex.getProvider())
                .with(ex.getDetails())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(problem);
    }

    private String extractField(List<JsonMappingException.Reference> path) {
        return path.isEmpty() ? "unknown" : path.get(path.size() - 1).getFieldName();
    }
}
