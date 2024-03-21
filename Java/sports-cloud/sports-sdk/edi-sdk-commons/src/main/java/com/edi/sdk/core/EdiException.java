package com.edi.sdk.core;

/**
 * SDK异常
 */
public class EdiException extends RuntimeException {

    public EdiException(String message) {
        super(message);
    }

    public EdiException(String message, Throwable cause) {
        super(message, cause);
    }
}
