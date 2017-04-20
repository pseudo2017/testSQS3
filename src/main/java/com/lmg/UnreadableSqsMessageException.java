package com.lmg;

public class UnreadableSqsMessageException extends RuntimeException {

    public UnreadableSqsMessageException(String message, Throwable cause) {
        super(message, cause);
    }	
}
