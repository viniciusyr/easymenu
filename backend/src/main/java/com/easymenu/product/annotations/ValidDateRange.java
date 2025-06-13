package com.easymenu.product.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateRangeValidator.class)
@Documented
public @interface ValidDateRange {
    String message() default "The end expiration date must be later than the start date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
