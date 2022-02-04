package com.hhovhann.optimisationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OptimisationNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4285341894354118776L;

    public OptimisationNotFoundException() {
        super();
    }

    public OptimisationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisationNotFoundException(String message) {
        super(message);
    }

    public OptimisationNotFoundException(Throwable cause) {
        super(cause);
    }
}