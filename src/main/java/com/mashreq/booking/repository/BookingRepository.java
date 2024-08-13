package com.mashreq.booking.repository;

import com.mashreq.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByRoomNameAndDate(String roomName, LocalDate date);

    @Query("SELECT b FROM BookingEntity b WHERE b.date = :date AND " +
            "(:startTime < b.endTime AND :endTime > b.startTime)")
    List<BookingEntity> findOverlappingBookings(@Param("date") LocalDate date,
                                                @Param("startTime") LocalTime startTime,
                                                @Param("endTime") LocalTime endTime);
}
