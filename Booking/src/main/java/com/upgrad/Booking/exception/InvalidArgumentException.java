package com.upgrad.Booking.exception;

/*
  Handles run-time Exception with Invalid Args which returns
    @param message Exception message
 */

public class InvalidArgumentException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    public InvalidArgumentException(String message) {
        super(message);
    }
}
