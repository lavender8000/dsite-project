package com.lav.dsite.exception;

import com.lav.dsite.enums.ResponseStatus;

public class UserServiceException extends DsiteServiceException {

    public UserServiceException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public UserServiceException(ResponseStatus responseStatus, Throwable cause) {
        super(responseStatus, cause);
    }

}
