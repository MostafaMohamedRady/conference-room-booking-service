package com.mashreq.booking.controller;

import com.mashreq.booking.dto.BookingRequestDto;
import com.mashreq.booking.entity.BookingEntity;
import com.mashreq.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/internal/v1/bookings")
@Tag(name = "Bookings", description = "endpoints to manage the room booking")
@AllArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @ApiResponse(responseCode = "200", description = "Book conference room", content = @Content(
            schema = @Schema(implementation = BookingRequestDto.class)
    ))
    @Operation(summary = "Book conference room",
            description = "<a href='' target='_blank'></a>")
    @ApiResponse(responseCode = "400", description = "Fields to not match validations")
    @ApiResponse(responseCode = "500", description = "Generic internal server error")
    @ApiResponse(responseCode = "502", description = "Server gateway error")
    @PostMapping
    public BookingEntity bookRoom(@Valid @RequestBody BookingRequestDto requestDto) {
        return bookingService.bookRoom(requestDto);
    }

    @ApiResponse(responseCode = "200", description = "Get Bookings", content = @Content(
            schema = @Schema(implementation = BookingEntity.class)
    ))
    @Operation(summary = "Listing bookings",
            description = "<a href='' target='_blank'></a>")
    @ApiResponse(responseCode = "400", description = "Fields to not match validations")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - Not ")
    @ApiResponse(responseCode = "500", description = "Generic internal server error")
    @ApiResponse(responseCode = "502", description = "Server gateway error")
    @GetMapping
    public List<BookingEntity> getBookings(@RequestParam @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Start time must be in the format HH:mm") String startTime,
                                           @RequestParam @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "End time must be in the format HH:mm") String endTime) {

        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        return bookingService.getBookings(date, start, end);
    }
}
