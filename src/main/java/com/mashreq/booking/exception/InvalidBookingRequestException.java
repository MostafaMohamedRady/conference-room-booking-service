package com.mashreq.booking.exception;

import lombok.Getter;

@Getter
public class InvalidBookingRequestException extends RuntimeException {
    private final String statusCode;
    public InvalidBookingRequestException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}