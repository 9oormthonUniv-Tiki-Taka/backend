package com.tikitaka.api.exception;

import org.springframework.security.core.AuthenticationException;

public class NeedsVerificationException extends AuthenticationException {
    private final String sub;

    public NeedsVerificationException(String sub) {
        super("NEEDS_VERIFICATION:" + sub);
        this.sub = sub;
    }

    public String getSub() {
        return sub;
    }
}