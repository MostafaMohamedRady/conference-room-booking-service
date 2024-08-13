package com.mashreq.booking.validation;

import com.mashreq.booking.exception.InvalidBookingRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingTimeValidatorTest {

    @InjectMocks
    private BookingTimeValidator bookingTimeValidator;

    @Test
    void testValidate_ValidInterval() {
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(14, 15);
        bookingTimeValidator.validate(startTime, endTime); // Should pass without exception
    }

    @Test
    void testValidate_InvalidInterval_StartTime() {
        LocalTime startTime = LocalTime.of(14, 1);
        LocalTime endTime = LocalTime.of(14, 15);
        assertThrows(InvalidBookingRequestException.class, () -> {
            bookingTimeValidator.validate(startTime, endTime);
        });
    }

    @Test
    void testValidate_InvalidInterval_EndTime() {
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(14, 14);
        assertThrows(InvalidBookingRequestException.class, () -> {
            bookingTimeValidator.validate(startTime, endTime);
        });
    }

    @Test
    void testValidate_StartTimeAfterEndTime() {
        LocalTime startTime = LocalTime.of(14, 30);
        LocalTime endTime = LocalTime.of(14, 15);
        assertThrows(InvalidBookingRequestException.class, () -> {
            bookingTimeValidator.validate(startTime, endTime);
        });
    }

    @Test
    void testValidate_StartTimeEqualsEndTime() {
        LocalTime startTime = LocalTime.of(14, 15);
        LocalTime endTime = LocalTime.of(14, 15);
        assertThrows(InvalidBookingRequestException.class, () -> {
            bookingTimeValidator.validate(startTime, endTime);
        });
    }

}