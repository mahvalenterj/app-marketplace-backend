package com.marketplace.domain.exceptions;

public class DomainException extends RuntimeException {
    private final String code;

    public DomainException(String message) {
        super(message);
        this.code = "DOMAIN_ERROR";
    }

    public DomainException(String message, String code) {
        super(message);
        this.code = code;
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
        this.code = "DOMAIN_ERROR";
    }

    public DomainException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
