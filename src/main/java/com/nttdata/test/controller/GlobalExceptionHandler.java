package com.nttdata.test.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail = null;
        exception.printStackTrace();

        if (exception instanceof BadRequestException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Bad Request Exception");
            errorDetail.setProperty("mensaje", exception.getMessage());
            return errorDetail;
        }

        if (exception instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), "Bad Credentials Exception");
            errorDetail.setProperty("mensaje", exception.getMessage());
            return errorDetail;
        }

        // Cualquier otro exception no controlada
        errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), "Internal Exception");
        errorDetail.setProperty("mensaje", exception.getMessage());
        return errorDetail;
    }
}
