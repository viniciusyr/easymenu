package com.easymenu.product.validations;

import com.easymenu.product.ProductRecordDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, ProductRecordDTO> {

    @Override
    public boolean isValid(ProductRecordDTO dto, ConstraintValidatorContext context) {
        if (dto.validityStart() == null || dto.validityEnd() == null) {
            return true;
        }

        boolean isValid = dto.validityStart().isBefore(dto.validityEnd());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("validation start must be before validation end")
                    .addPropertyNode("validityStart")
                    .addConstraintViolation();
        }

        return isValid;
    }

}
