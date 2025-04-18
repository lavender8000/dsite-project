package com.lav.dsite.enums;

public enum OauthProvider {
    GOOGLE;

    public static OauthProvider fromString(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        try {
            return OauthProvider.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid OauthProvider: " + name);
        }
    }
}
