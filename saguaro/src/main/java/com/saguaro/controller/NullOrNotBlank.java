package com.saguaro.controller;

import javax.validation.Constraint;
import javax.validation.Payload;
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
     * Return a message in case of unsuccessful validation. The default message, if one is not provided
     * with the annotation, is "Field must be null or contain at least one non-whitespace character".
     *
     * @return a message detailing why this validator failed
     */
    public String message() default "Field must be null or contain at least one non-whitespace character";

    /**
     * Returns the validation groups this annotation is being used with
     *
     * @return an array of Class objects representing the validation groups this annotation is being used with
     */
    Class<?>[] groups() default {};

    /**
     * Returns the payload attached to this validation annotation.
     *
     * @return the payload attached to this annotation
     */
    public Class<? extends Payload>[] payload() default {};
}
