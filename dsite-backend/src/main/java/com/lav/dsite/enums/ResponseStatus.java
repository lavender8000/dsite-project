package com.lav.dsite.enums;

public enum ResponseStatus {

    SUCCESS(200, "Success"),
    CREATED(201, "Created"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    
    USER_EMAIL_EXISTS(409, "Email already exists"),
    USER_OAUTH_ACCOUNT_EXISTS(409, "User oauth account already exists"),

    POST_SAVE_FAILED(400, "Post save failed"),
    USER_PASSWORD_ALREADY_SET(400, "Password already set"),

    USER_NOT_FOUND(404, "User not found"),
    POST_NOT_FOUND(404, "Post not found"),
    FORUM_NOT_FOUND(404, "Forum not found"),

    OPTIMISTIC_LOCK_EXCEPTION(409, "Optimistic Lock Exception"),
    
    USER_PASSWORD_NOT_MATCHED(401, "Password not matched"),
    LOGIN_FAILED(401, "Login failed"),
    INVALID_JWT_TOKEN(401, "Invalid token"),
    JWT_EXPIRED(401, "Token expired"),

    INVALID_REQUEST(400, "Invalid request"),
    INVALID_ARGUMENT(400, "Invalid argument provided"),
    UNSUPPORTED_OAUTH_PROVIDER(400, "Unsupported oauth provider"),
    DATABASE_ERROR(500, "Database operation failed"),
    REDIS_ERROR(500, "Redis operation failed"),
    IO_ERROR(500, "I/O operation failed"),
    
    INTERNAL_SERVER_ERROR(500, "Internal server error");


    

    private final int code;
    private final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
