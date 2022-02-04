package com.hhovhann.optimisationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CampaignNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4285341894354118776L;

    public CampaignNotFoundException() {
        super();
    }

    public CampaignNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CampaignNotFoundException(String message) {
        super(message);
    }

    public CampaignNotFoundException(Throwable cause) {
        super(cause);
    }
}