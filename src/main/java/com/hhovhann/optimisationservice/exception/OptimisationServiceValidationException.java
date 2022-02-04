package com.hhovhann.optimisationservice.exception;

public class OptimisationServiceValidationException extends RuntimeException {
    private static final long serialVersionUID = -9038935358708640825L;

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