package com.mashreq.booking.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomNotAvailableException.class)
    public ResponseEntity<ErrorMessage> handleRoomNotAvailableException(RoomNotAvailableException e) {
        log.error("RoomNotAvailableExceptionHandler [{}]", e.getMessage(), e);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .timestamp(new Date()).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBookingRequestException.class)
    public ResponseEntity<ErrorMessage> handleInvalidBookingRequestException(InvalidBookingRequestException e) {
        log.error("InvalidBookingRequestException [{}]", e.getMessage(), e);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(e.getMessage())
                .statusCode(e.getStatusCode())
                .timestamp(new Date()).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException e) {
        log.error("handleValidationException [{}]", e.getMessage());
        StringBuilder msg = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            msg.append("[FieldName:-").append(fieldName).append(", ");
            msg.append("ErrorMessage:-").append(errorMessage).append("]");
        });

        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(msg.toString())
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .timestamp(new Date()).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception e) {
        log.error("globalExceptionHandler [{}]", e.getMessage(), e);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .timestamp(new Date()).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}