package com.lav.dsite.exception;

import com.lav.dsite.enums.ResponseStatus;

public class PostServiceException extends DsiteServiceException {

    public PostServiceException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public PostServiceException(ResponseStatus responseStatus, Throwable cause) {
        super(responseStatus, cause);
    }

}
