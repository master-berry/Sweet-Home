package com.upgrad.Booking.exception.handler;

import com.upgrad.Booking.exception.InvalidArgumentException;
import com.upgrad.Booking.exception.dto.ErrorResponse;
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

    /**
     * Handles InvalidArgumentException and returns a ResponseEntity with error details.
     * Constructs an ErrorResponse with the exception message and HTTP status code,
     * adds it to a list of error details, and returns the list with HTTP status BAD_REQUEST.
     *
     * @param e The InvalidArgumentException thrown.
     * @param request The WebRequest in which the exception occurred.
     * @return ResponseEntity with a list of error details and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity handleInvalidArgumentException(InvalidArgumentException e, WebRequest request) {
        List<ErrorResponse> errorDetails = new ArrayList<>();
        ErrorResponse error = new ErrorResponse(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value());
        errorDetails.add(error);

        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
