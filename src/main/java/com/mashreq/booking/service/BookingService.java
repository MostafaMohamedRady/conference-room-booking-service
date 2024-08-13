package com.mashreq.booking.service;

import com.mashreq.booking.dto.AvailableBookingsResponseDto;
import com.mashreq.booking.dto.BookingRequestDto;
import com.mashreq.booking.dto.BookingResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {
    BookingResponseDto bookRoom(BookingRequestDto requestDto);

    AvailableBookingsResponseDto getAvailableMeetingRooms(LocalDate date, LocalTime start, LocalTime end);
}
