package com.liraz.classmanagement.dtos.errors;

import org.springframework.http.HttpStatus;

public class ErrorDTO {
    private int code;
    private String message;

    public ErrorDTO(int code,String s) {
        this.code = code;
        this.message = s;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
