package com.hhovhann.optimisationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecomendationNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4285341894354118776L;

    public RecomendationNotFoundException() {
        super();
    }

    public RecomendationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecomendationNotFoundException(String message) {
        super(message);
    }

    public RecomendationNotFoundException(Throwable cause) {
        super(cause);
    }
}