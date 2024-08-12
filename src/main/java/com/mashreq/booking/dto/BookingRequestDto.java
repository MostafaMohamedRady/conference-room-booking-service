package com.mashreq.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "BookingRequestDto")
public class BookingRequestDto {

    @Schema(name = "attendees", description = "Number of People Attending.")
    @NotNull(message = "Number of attendees is required")
    @Min(value = 1, message = "Number of attendees must be at least 1.")
    private int attendees;

    @Schema(name = "startTime", description = "Meeting Start Time.")
    @NotNull(message = "Start time is required")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Start time must be in the format HH:mm")
    private String startTime;

    @Schema(name = "endTime", description = "Meeting End Time.")
    @NotNull(message = "End time is required")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "End time must be in the format HH:mm")
    private String endTime;
}
