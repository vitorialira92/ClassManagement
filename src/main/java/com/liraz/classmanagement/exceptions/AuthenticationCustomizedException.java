package com.liraz.classmanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthenticationCustomizedException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;


    public AuthenticationCustomizedException(String message) {
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
