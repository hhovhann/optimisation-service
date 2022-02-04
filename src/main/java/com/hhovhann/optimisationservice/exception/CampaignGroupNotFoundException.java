package com.hhovhann.optimisationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CampaignGroupNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5174305269330666461L;

    public CampaignGroupNotFoundException() {
        super();
    }

    public CampaignGroupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CampaignGroupNotFoundException(String message) {
        super(message);
    }

    public CampaignGroupNotFoundException(Throwable cause) {
        super(cause);
    }
}