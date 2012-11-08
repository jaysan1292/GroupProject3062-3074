package com.jaysan1292.groupproject.exceptions;

import javax.xml.ws.WebFault;

/** @author Jason Recillo */
@WebFault(faultBean = "com.jaysan1292.groupproject.service.exceptions.FaultBean")
public class LoginFaultException extends BaseFaultException {
    public LoginFaultException() {
        super("There was a problem logging in.");
    }

    public LoginFaultException(String message) {
        super(message);
    }

    public LoginFaultException(String message, Fault faultInfo) {
        super(message, faultInfo);
    }

    public LoginFaultException(String message, Fault faultInfo, Throwable cause) {
        super(message, faultInfo, cause);
    }
}
