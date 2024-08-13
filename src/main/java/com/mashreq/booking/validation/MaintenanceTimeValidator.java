package com.mashreq.booking.validation;

import com.mashreq.booking.dto.BookingRequestDto;
import com.mashreq.booking.exception.InvalidBookingRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class MaintenanceTimeValidator {

    private final List<LocalTime[]> maintenanceTimings = Arrays.asList(
            new LocalTime[]{LocalTime.of(9, 0), LocalTime.of(9, 15)},
            new LocalTime[]{LocalTime.of(13, 0), LocalTime.of(13, 15)},
            new LocalTime[]{LocalTime.of(17, 0), LocalTime.of(17, 15)}
    );


    public void validate(LocalTime startTime, LocalTime endTime) {
        log.info("Validate Maintenance Time");
        for (LocalTime[] maintenanceTime : maintenanceTimings) {
            LocalTime maintenanceStart = maintenanceTime[0];
            LocalTime maintenanceEnd = maintenanceTime[1];

            if ((startTime.isBefore(maintenanceEnd) && endTime.isAfter(maintenanceStart))) {
                log.error("Booking cannot be done during maintenance time, start {} - end {}.", maintenanceStart, maintenanceEnd);
                throw new InvalidBookingRequestException("Booking cannot be done during maintenance time.", "MAINTENANCE_TIME_ERROR");
            }
        }
    }
}
