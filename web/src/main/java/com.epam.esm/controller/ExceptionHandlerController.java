package com.epam.esm.controller;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ServiceException> handleServiceException(ServiceException exception) {
        if (exception.getMessage().contains("already exists")){
            return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServiceException> handleIllegalArgumentException() {
        ServiceException myException = new ServiceException("Invalid param passed in a request",
                ErrorCodeEnum.INVALID_INPUT);
        return new ResponseEntity<>(myException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServiceException> handleServiceException() {
        ServiceException exception = new ServiceException("Invalid field type passed in a JSON",
                ErrorCodeEnum.INVALID_INPUT);
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
