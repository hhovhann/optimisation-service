package com.hhovhann.optimisationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CampaignGroupNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4285341894354118776L;

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