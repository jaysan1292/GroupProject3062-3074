package com.jaysan1292.groupproject.exceptions;

/** @author Jason Recillo */
public class NotAuthorizedFaultException extends BaseFaultException {
    public NotAuthorizedFaultException() {
        super("The user is not authorized to call this service method.");
    }

    public NotAuthorizedFaultException(String message) {
        super(message);
    }

    public NotAuthorizedFaultException(String message, Fault faultInfo) {
        super(message, faultInfo);
    }

    public NotAuthorizedFaultException(String message, Fault faultInfo, Throwable cause) {
        super(message, faultInfo, cause);
    }
}
