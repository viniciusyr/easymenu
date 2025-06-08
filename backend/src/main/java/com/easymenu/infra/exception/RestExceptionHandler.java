package com.easymenu.infra.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Problem> userNotFoundHandler(GlobalException exception){
        Problem problem = Problem.builder()
                .withType(URI.create("https://easymenu.app/problems/" + exception.getType()))
                .withTitle(exception.getTitle())
                .withStatus(exception.getStatus())
                .withDetail(exception.getMessage())
                .build();
        return ResponseEntity.status(exception.getStatus().getStatusCode()).body(problem);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleGeneric(Exception exception) {
        Problem problem = Problem.builder()
                .withType(URI.create("https://easymenu.app/problems/internal-error"))
                .withTitle("Internal Server Error")
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withDetail("An unexpected error occurred. Please contact support.")
                .build();

        log.error("Unexpected error", exception);

        return ResponseEntity.status(500).body(problem);
    }
}
