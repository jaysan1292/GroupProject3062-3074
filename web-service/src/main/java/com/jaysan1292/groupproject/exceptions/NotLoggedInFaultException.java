package com.jaysan1292.groupproject.exceptions;

import javax.xml.ws.WebFault;

/** @author Jason Recillo */
@WebFault(faultBean = "com.jaysan1292.groupproject.exceptions.Fault")
public class NotLoggedInFaultException extends LoginFaultException {

    public NotLoggedInFaultException() {
        super("The user is not logged in.");
    }

    public NotLoggedInFaultException(String message) {
        super(message);
    }

    public NotLoggedInFaultException(String message, Fault faultInfo) {
        super(message, faultInfo);
    }

    public NotLoggedInFaultException(String message, Fault faultInfo, Throwable cause) {
        super(message, faultInfo, cause);
    }
}
