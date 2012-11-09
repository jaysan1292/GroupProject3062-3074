package com.jaysan1292.groupproject.exceptions;

/** @author Jason Recillo */
public abstract class BaseFaultException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Fault faultInfo;

    public BaseFaultException(String message) {
        super(message);
        this.faultInfo = new Fault();
    }

    public BaseFaultException(String message, Fault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public BaseFaultException(String message, Fault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public Fault getFaultInfo() {
        return faultInfo;
    }
}
