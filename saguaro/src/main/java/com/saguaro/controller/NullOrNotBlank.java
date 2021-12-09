package com.saguaro.controller;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for validating fields as either null or not blank (must contain at least one non-whitespace
 * character).
 *
 * @author Charles Wong
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {

    /**
     * Returns the validation groups this annotation is being used with
     *
     * @return an array of Class objects representing the validation groups this annotation is being used with
     */
    Class<?>[] groups() default {};
}
