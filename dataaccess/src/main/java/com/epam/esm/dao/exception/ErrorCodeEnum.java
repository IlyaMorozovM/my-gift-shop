package com.epam.esm.dao.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCodeEnum {

    FAILED_TO_RETRIEVE_CERTIFICATE(1001),
    FAILED_TO_DELETE_CERTIFICATE(1002),
    FAILED_TO_UPDATE_CERTIFICATE(1003),
    FAILED_TO_ADD_CERTIFICATE(1004),
    FAILED_TO_RETRIEVE_TAG(2001),
    FAILED_TO_DELETE_TAG(2002),
    FAILED_TO_ADD_TAG(2003),
    INVALID_INPUT(3001),
    INVALID_SORT_INPUT(3002),
    CERTIFICATE_VALIDATION_ERROR(4003),
    TAG_VALIDATION_ERROR(4004);

    private final int code;

    ErrorCodeEnum(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}
