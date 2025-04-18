package com.lav.dsite.enums;

public enum JwtClaimKey {

    TOKEN_TYPE("token_type"),
    AUTH_TYPE("auth_type"),
    EMAIL("email"),
    NICKNAME("nickname");

    private final String key;

    JwtClaimKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
