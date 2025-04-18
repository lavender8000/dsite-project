package com.lav.dsite.exception;

import com.lav.dsite.enums.ResponseStatus;

public class OauthAccountServiceException extends DsiteServiceException {

    public OauthAccountServiceException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public OauthAccountServiceException(ResponseStatus responseStatus, Throwable cause) {
        super(responseStatus, cause);
    }
    
}
