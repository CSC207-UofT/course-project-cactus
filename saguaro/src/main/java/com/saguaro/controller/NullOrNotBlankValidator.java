package com.saguaro.controller;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This class provides the implementation for a custom null or not-blank validator.
 *
 * @author Charles Wong
 */
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    /**
     * Initialize this validator. Specifically, there are no actions to perform for this validator's
     * initialization.
     *
     * @param parameters a NullOrNotBlank annotation instance for the constraint declaration
     */
    public void initialize(NullOrNotBlank parameters) {
    }

    /**
     * Validate some value as either null or not blank (contains at least one non-whitespace character)
     *
     * @param value                      the String value to validated
     * @param constraintValidatorContext the ConstraintValidatorContext this constraint is being evaluated in
     * @return true if validation was performed successfully, false otherwise
     */
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || value.trim().length() > 0;
    }
}
