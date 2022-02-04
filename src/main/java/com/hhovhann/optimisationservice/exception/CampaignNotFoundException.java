package com.hhovhann.optimisationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CampaignNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1315501090292134556L;

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