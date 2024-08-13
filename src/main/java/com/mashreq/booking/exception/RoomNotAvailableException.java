package com.mashreq.booking.exception;

import lombok.Getter;

@Getter
public class RoomNotAvailableException extends RuntimeException {
    private final String statusCode;

    public RoomNotAvailableException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}