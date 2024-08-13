package com.mashreq.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "BookingResponse")
public class BookingResponseDto {

    @Schema(name = "bookingReference", description = "Booking reference number.")
    private String bookingReference;

    @Schema(name = "bookingId", description = "Room Name.")
    private String roomName;

    @Schema(name = "startTime", description = "Meeting Start Time.")
    private String startTime;

    @Schema(name = "endTime", description = "Meeting End Time.")
    private String endTime;

    @Schema(name = "attendees", description = "Number of People Attending.")
    private Integer numberOfPeople;

    @Schema(name = "status", description = "Booking Status.")
    private String status;
}
