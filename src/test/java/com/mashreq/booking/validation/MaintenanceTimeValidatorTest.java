package com.mashreq.booking.validation;

import com.mashreq.booking.exception.InvalidBookingRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class MaintenanceTimeValidatorTest {

    @InjectMocks
    private MaintenanceTimeValidator maintenanceTimeValidator;

    @Test
    void testValidate_ValidBookingTime() {
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(10, 15);
        maintenanceTimeValidator.validate(startTime, endTime); // Should pass without exception
    }

    @Test
    void testValidate_BookingOverlapsMaintenanceTime_Morning() {
        LocalTime startTime = LocalTime.of(8, 45);
        LocalTime endTime = LocalTime.of(9, 5);
        assertThrows(InvalidBookingRequestException.class, () -> {
            maintenanceTimeValidator.validate(startTime, endTime);
        });
    }

    @Test
    void testValidate_BookingOverlapsMaintenanceTime_Afternoon() {
        LocalTime startTime = LocalTime.of(12, 50);
        LocalTime endTime = LocalTime.of(13, 5);
        assertThrows(InvalidBookingRequestException.class, () -> {
            maintenanceTimeValidator.validate(startTime, endTime);
        });
    }

    @Test
    void testValidate_BookingOverlapsMaintenanceTime_Evening() {
        LocalTime startTime = LocalTime.of(16, 55);
        LocalTime endTime = LocalTime.of(17, 10);
        assertThrows(InvalidBookingRequestException.class, () -> {
            maintenanceTimeValidator.validate(startTime, endTime);
        });
    }

    @Test
    void testValidate_BookingDuringMaintenanceTime() {
        LocalTime startTime = LocalTime.of(13, 0);
        LocalTime endTime = LocalTime.of(13, 15);
        assertThrows(InvalidBookingRequestException.class, () -> {
            maintenanceTimeValidator.validate(startTime, endTime);
        });
    }

    @Test
    void testValidate_BookingJustBeforeMaintenanceTime() {
        LocalTime startTime = LocalTime.of(8, 45);
        LocalTime endTime = LocalTime.of(9, 0);
        maintenanceTimeValidator.validate(startTime, endTime); // Should pass without exception
    }

    @Test
    void testValidate_BookingJustAfterMaintenanceTime() {
        LocalTime startTime = LocalTime.of(9, 15);
        LocalTime endTime = LocalTime.of(9, 30);
        maintenanceTimeValidator.validate(startTime, endTime); // Should pass without exception
    }
}
