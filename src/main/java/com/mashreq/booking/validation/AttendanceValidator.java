package com.mashreq.booking.validation;

import com.mashreq.booking.exception.InvalidBookingRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Slf4j
@Service
public class AttendanceValidator {

    /**
     * The number of people allowed for booking should be greater than 1 and less
     * than or equal to the maximum capacity.
     * @param startTime
     * @param endTime
     */
    public void validate(LocalTime startTime, LocalTime endTime) {
        log.info("Validate Booking Time");
        if (!startTime.isBefore(endTime)){
            log.error("Booking Start Time must be lesser than End Time.");
            throw new InvalidBookingRequestException("Booking Start Time must be lesser than End Time.", "BOOKING_TIME_ERROR");
        }
        if (isValidInterval(startTime) || isValidInterval(endTime)){
            log.error("Booking time should given by the user in intervals of 15 minutes.");
            throw new InvalidBookingRequestException("Booking time should given by the user in intervals of 15 minutes.", "BOOKING_TIME_ERROR");
        }
    }

    private boolean isValidInterval(LocalTime time) {
        return time.getMinute() % 15 != 0;
    }
}
