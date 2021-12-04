package com.saguaro.controller;

import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import com.saguaro.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception handler for Saguaro. This class provides methods that intercept exceptions
 * thrown by controllers, and responds accordingly.
 *
 * Handled exceptions include:
 * <ul>
 *     <li>InvalidLoginException
 *     <li>InvalidParamException
 *     <li>ResourceNotFoundException
 * </ul>
 *
 * @author Charles Wong
 */
@ControllerAdvice
public class SaguaroExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handler for any InvalidLoginException that is thrown. This exception will be
     * thrown if invalid login credentials are provided.
     *
     * Returns a 401 UNAUTHORIZED response with the exception message.
     *
     * @param e       the InvalidLoginException to be handled
     * @param request the request which generated this exception
     * @return a response with 401 UNAUTHORIZED status
     * @see InvalidLoginException
     */
    @ExceptionHandler(value = {
            InvalidLoginException.class
    })
    ResponseEntity<Object> handle(InvalidLoginException e, WebRequest request) {
        // TODO: return WWW-Authenticate header?

        return handleExceptionInternal(e,
                e.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * Handler for any InvalidParamException that is thrown. Requests that thrown this
     * exception include, but are not limited to, those that contain invalid payloads.
     *
     * Returns a 409 CONFLICT response with the exception message.
     *
     * @param e       the InvalidParamException to be handled
     * @param request the request which generated this exception
     * @return a response with 409 CONFLICT status
     * @see InvalidParamException
     */
    @ExceptionHandler(value = {
            InvalidParamException.class
    })
    ResponseEntity<Object> handle(InvalidParamException e, WebRequest request) {

        return handleExceptionInternal(e,
                e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handler for any ResourceNotFoundException. This exception is thrown when a
     * requests attempts to access a resource that either does not exist, or is a
     * secret the client is not authorized to access.
     *
     * Returns a 404 NOT_FOUND with the exception message.
     *
     * @param e       the ResourceNotFoundException to be handled
     * @param request the request which generated this exception
     * @return a response with 404 NOT_FOUND status
     * @see ResourceNotFoundException
     */
    @ExceptionHandler(value = {
            ResourceNotFoundException.class
    })
    ResponseEntity<Object> handle(ResourceNotFoundException e, WebRequest request) {

        return handleExceptionInternal(e,
                e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
