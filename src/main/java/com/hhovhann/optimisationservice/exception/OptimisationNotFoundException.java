package com.hhovhann.optimisationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OptimisationNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3085079476268412169L;

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