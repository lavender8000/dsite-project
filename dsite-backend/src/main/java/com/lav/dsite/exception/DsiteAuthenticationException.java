package com.lav.dsite.exception;

import org.springframework.security.core.AuthenticationException;

import com.lav.dsite.enums.ResponseStatus;

public class DsiteAuthenticationException extends AuthenticationException {

    private final ResponseStatus responseStatus;

    public DsiteAuthenticationException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }

    public DsiteAuthenticationException(ResponseStatus responseStatus, Throwable cause) {
        super(responseStatus.getMessage(), cause);
        this.responseStatus = responseStatus;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }
}
