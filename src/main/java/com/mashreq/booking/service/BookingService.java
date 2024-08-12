package com.mashreq.booking.service;

import com.mashreq.booking.dto.BookingRequestDto;
import com.mashreq.booking.entity.BookingEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingService {
    BookingEntity bookRoom(BookingRequestDto requestDto);
    List<BookingEntity> getBookings(LocalDate date, LocalTime startTime, LocalTime endTime);
}
