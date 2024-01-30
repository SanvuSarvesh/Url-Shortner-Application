package com.example.UrlShortner.exception.controller;

import org.springframework.http.HttpStatus;

public class UrlExceptions extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus httpStatus;

    public UrlExceptions(HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    public UrlExceptions(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}