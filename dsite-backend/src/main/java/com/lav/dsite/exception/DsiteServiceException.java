package com.lav.dsite.exception;

import com.lav.dsite.enums.ResponseStatus;

public class DsiteServiceException extends RuntimeException {

    private final ResponseStatus responseStatus;

    public DsiteServiceException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }

    public DsiteServiceException(ResponseStatus responseStatus, Throwable cause) {
        super(responseStatus.getMessage(), cause);
        this.responseStatus = responseStatus;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

}
