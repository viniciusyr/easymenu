package com.easymenu.infra.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.violations.Violation;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class CustomConstraintViolationProblem extends AbstractThrowableProblem {

    public CustomConstraintViolationProblem(
            String detail,
            List<Violation> violations
    ) {
        super(
                URI.create("https://easymenu.app/problems/validation-error"),
                "Validation Failed",
                Status.BAD_REQUEST,
                detail,
                null,
                null,
                Map.of("violations", violations, "timestamp", Instant.now())
        );
    }
}
