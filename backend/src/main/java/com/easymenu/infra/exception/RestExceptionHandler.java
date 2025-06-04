package com.easymenu.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zalando.problem.Problem;

import java.net.URI;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    private ResponseEntity<Problem> userNotFoundHandler(GlobalException exception){
        Problem problem = Problem.builder()
                .withType(URI.create("https://easymenu.app/problems/" + exception.getType()))
                .withTitle(exception.getTitle())
                .withStatus(exception.getStatus())
                .withDetail(exception.getMessage())
                .build();
        return ResponseEntity.status(exception.getStatus().getStatusCode()).body(problem);
    }
}
