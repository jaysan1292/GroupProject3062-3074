package com.jaysan1292.groupproject.service.security;

/**
 * Created with IntelliJ IDEA.
 * Date: 02/12/12
 * Time: 4:24 PM
 *
 * @author Jason Recillo
 */
public class AuthorizationException extends Exception {
    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
