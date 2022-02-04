package com.hhovhann.optimisationservice.exception;

import java.io.Serial;

public class OptimisationServiceValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1365449883298233191L;

    public OptimisationServiceValidationException() {
        super();
    }

    public OptimisationServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisationServiceValidationException(String message) {
        super(message);
    }

    public OptimisationServiceValidationException(Throwable cause) {
        super(cause);
    }
}