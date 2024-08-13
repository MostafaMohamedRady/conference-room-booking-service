package com.mashreq.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Schema(description = "AvailableBookingsResponseDto")
public class AvailableBookingsResponseDto {
    private List<AvailableRoomDto> availableRooms;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public static class AvailableRoomDto {
        private String roomName;
        private Integer maxCapacity;
    }
}
