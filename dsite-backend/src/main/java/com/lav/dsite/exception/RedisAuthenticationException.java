package com.lav.dsite.exception;

import com.lav.dsite.enums.ResponseStatus;

public class RedisAuthenticationException extends DsiteAuthenticationException {

    public RedisAuthenticationException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public RedisAuthenticationException(ResponseStatus responseStatus, Throwable cause) {
        super(responseStatus, cause);
    }

}
