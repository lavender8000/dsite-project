package com.lav.dsite.exception;

import com.lav.dsite.enums.ResponseStatus;

public class AuthServiceException extends DsiteServiceException {

    public AuthServiceException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public AuthServiceException(ResponseStatus responseStatus, Throwable cause) {
        super(responseStatus, cause);
    }
}
