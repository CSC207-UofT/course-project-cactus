package com.saguaro.controller;

import com.saguaro.exception.InvalidLoginException;
import com.saguaro.exception.InvalidParamException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SaguaroExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handler for any InvalidLoginException that is thrown.
     *
     * @param e the InvalidLoginException to be handled
     * @param request the request which generated this exception
     * @return the response with the appropriate HTTP status code
     */
    @ExceptionHandler(value = {
            InvalidLoginException.class
    })
    ResponseEntity<Object> handle(InvalidLoginException e, WebRequest request) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.put(HttpHeaders.WWW_AUTHENTICATE, List.of("Basic"));

        return handleExceptionInternal(e,
                e.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = {
            InvalidParamException.class
    })
    ResponseEntity<Object> handle(InvalidParamException e, WebRequest request) {

        return handleExceptionInternal(e,
                e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


}
