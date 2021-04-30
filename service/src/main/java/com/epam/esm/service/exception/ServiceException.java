package com.epam.esm.service.exception;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"stackTrace", "cause", "suppressed", "localizedMessage"}, allowSetters = true)
public class ServiceException extends Exception {

    private final ErrorCodeEnum errorCode;

    public ServiceException(ErrorCodeEnum errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(String message, ErrorCodeEnum errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, Throwable cause, ErrorCodeEnum errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause, ErrorCodeEnum errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}
