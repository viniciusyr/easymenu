package com.easymenu.infra.exception;

import com.easymenu.authentication.exceptions.AuthenticationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidation(MethodArgumentNotValidException ex) {
        List<Violation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        CustomConstraintViolationProblem problem =
                new CustomConstraintViolationProblem("One or more fields are invalid.", violations);

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Problem> handleGlobal(GlobalException ex) {
        Problem problem = Problem.builder()
                .withType(URI.create("https://easymenu.app/problems/" + ex.getType()))
                .withTitle(ex.getTitle())
                .withStatus(ex.getStatus())
                .withDetail(ex.getMessage())
                .with("timestamp", Instant.now())
                .build();

        return ResponseEntity
                .status(ex.getStatus().getStatusCode())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
       public ResponseEntity<Problem> handleNotReadable(HttpMessageNotReadableException exception) {
        Throwable rootCause = ExceptionUtils.getRootCause(exception);

        if(rootCause instanceof InvalidFormatException invalidFormat){
            String field = extractField(invalidFormat.getPath());
            String value = String.valueOf(invalidFormat.getValue());

            Problem problem = Problem.builder().
                    withType(URI.create("https://easymenu.app/problems/invalid-format"))
                    .withTitle("Invalid Format")
                    .withStatus(Status.BAD_REQUEST)
                    .withDetail("Wrong type value for the field:" + field)
                    .with("timestamp", Instant.now())
                    .with("Invalid value", value)
                    .build();

            return ResponseEntity.badRequest().body(problem);
        }

        Problem genericProblem = Problem.builder().
                withType(URI.create("https://easymenu.app/problems/json-error"))
                .withTitle("Error JSON")
                .withStatus(Status.BAD_REQUEST)
                .withDetail("The submitted JSON could not be processed")
                .build();

        return ResponseEntity.badRequest().body(genericProblem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleGeneric(Exception ex) {
        Problem problem = Problem.builder()
                .withType(URI.create("https://easymenu.app/problems/internal-error"))
                .withTitle("Internal Error")
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withDetail("An unexpected error occurred.")
                .with("timestamp", Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    private String extractField(List<JsonMappingException.Reference> path) {
        return path.isEmpty() ? "unknown" : path.get(path.size() -1).getFieldName();
    }

}
