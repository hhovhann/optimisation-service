package com.hhovhann.optimisationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecommendationNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6336083374238658612L;

    public RecommendationNotFoundException() {
        super();
    }

    public RecommendationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecommendationNotFoundException(String message) {
        super(message);
    }

    public RecommendationNotFoundException(Throwable cause) {
        super(cause);
    }
}