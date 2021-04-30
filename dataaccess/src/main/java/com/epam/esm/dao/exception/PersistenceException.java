package com.epam.esm.dao.exception;


public class PersistenceException extends Exception {

    private final ErrorCodeEnum errorCode;

    public PersistenceException(ErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public PersistenceException(String message, ErrorCodeEnum errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PersistenceException(String message, Throwable cause, ErrorCodeEnum errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public PersistenceException(Throwable cause, ErrorCodeEnum errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }

}
