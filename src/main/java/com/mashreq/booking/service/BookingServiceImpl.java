package com.mashreq.booking.service;

import com.mashreq.booking.dto.BookingRequestDto;
import com.mashreq.booking.entity.BookingEntity;
import com.mashreq.booking.entity.ConferenceRoomEntity;
import com.mashreq.booking.exception.RoomNotAvailableException;
import com.mashreq.booking.repository.BookingRepository;
import com.mashreq.booking.repository.ConferenceRoomRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ConferenceRoomRepository conferenceRoomRepository;

    private Optional<ConferenceRoomEntity> findAvailableRoom(int attendees, LocalTime startTime, LocalTime endTime) {
        log.debug("Finding available room for {} attendees from {} to {}", attendees, startTime, endTime);

        List<ConferenceRoomEntity> suitableRooms = conferenceRoomRepository.findAll().stream()
                .filter(room -> room.getCapacity() >= attendees)
                .collect(Collectors.toList());

        for (ConferenceRoomEntity room : suitableRooms) {
            List<BookingEntity> roomBookings = bookingRepository.findByRoomNameAndDate(room.getName(), LocalDate.now());
            boolean isAvailable = roomBookings.stream().noneMatch(booking ->
                    (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime())) ||
                            (startTime.isBefore(LocalTime.of(9, 15)) && endTime.isAfter(LocalTime.of(9, 0))) ||
                            (startTime.isBefore(LocalTime.of(13, 15)) && endTime.isAfter(LocalTime.of(13, 0))) ||
                            (startTime.isBefore(LocalTime.of(17, 15)) && endTime.isAfter(LocalTime.of(17, 0)))
            );

            if (isAvailable) {
                log.debug("Room {} is available", room.getName());
                return Optional.of(room);
            }
        }

        log.warn("No available rooms for {} attendees from {} to {}", attendees, startTime, endTime);
        return Optional.empty();
    }

    @Override
    @Transactional
    public BookingEntity bookRoom(BookingRequestDto requestDto) {
        log.info("Booking room for {} attendees from {} to {}", requestDto.getAttendees(), requestDto.getStartTime(), requestDto.getEndTime());

        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.parse(requestDto.getStartTime());
        LocalTime end = LocalTime.parse(requestDto.getEndTime());

        Optional<ConferenceRoomEntity> availableRoom = findAvailableRoom(requestDto.getAttendees(), start, end);

        if (availableRoom.isPresent()) {
            BookingEntity booking = new BookingEntity();
            booking.setRoomName(availableRoom.get().getName());
            booking.setDate(date);
            booking.setStartTime(start);
            booking.setEndTime(end);
            booking.setAttendees(requestDto.getAttendees());
            log.info("Room {} booked successfully", availableRoom.get().getName());
            return bookingRepository.save(booking);
        } else {
            log.error("No available room for the specified time and attendees.");
            throw new RoomNotAvailableException("No available room for the specified time and attendees.");
        }
    }

    @Override
    public List<BookingEntity> getBookings(LocalDate date, LocalTime startTime, LocalTime endTime) {
        log.debug("Getting bookings on {} from {} to {}", date, startTime, endTime);
        return bookingRepository.findByDateAndStartTimeBeforeAndEndTimeAfter(date, startTime, endTime);
    }
}
