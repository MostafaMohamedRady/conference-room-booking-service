package com.mashreq.booking.repository;

import com.mashreq.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByRoomNameAndDate(String roomName, LocalDate date);

    List<BookingEntity> findByDateAndStartTimeBeforeAndEndTimeAfter(LocalDate date, LocalTime startTime, LocalTime endTime);
}
