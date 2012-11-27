package com.jaysan1292.groupproject.exceptions;

/** @author Jason Recillo */
public class GeneralServiceException extends Exception {
    public GeneralServiceException() {
    }

    public GeneralServiceException(String message) {
        super(message);
    }

    public GeneralServiceException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }

    public GeneralServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
