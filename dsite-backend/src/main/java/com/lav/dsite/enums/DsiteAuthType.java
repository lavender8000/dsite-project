package com.lav.dsite.enums;

public enum DsiteAuthType {
    PASSWORD("password"),
    OAUTH("oauth");

    private final String value;

    DsiteAuthType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
