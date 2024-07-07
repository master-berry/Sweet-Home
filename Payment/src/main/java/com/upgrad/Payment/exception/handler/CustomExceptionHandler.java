package com.upgrad.Payment.exception.handler;

import com.upgrad.Payment.exception.InvalidArgumentException;
import com.upgrad.Payment.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity handleInvalidArgumentException(InvalidArgumentException e,

                                                                         WebRequest request){
        List<ErrorResponse> errorDetails = new ArrayList<>();
        ErrorResponse error = new ErrorResponse(e.getLocalizedMessage(),HttpStatus.BAD_REQUEST.value());
        errorDetails.add(error);

        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }
}