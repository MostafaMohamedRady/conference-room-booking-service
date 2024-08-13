package com.mashreq.booking.service;

import com.mashreq.booking.dto.AvailableBookingsResponseDto;
import com.mashreq.booking.dto.BookingRequestDto;
import com.mashreq.booking.dto.BookingResponseDto;
import com.mashreq.booking.entity.BookingEntity;
import com.mashreq.booking.entity.ConferenceRoomEntity;
import com.mashreq.booking.exception.RoomNotAvailableException;
import com.mashreq.booking.mapper.BookingsMapper;
import com.mashreq.booking.repository.BookingRepository;
import com.mashreq.booking.repository.ConferenceRoomRepository;
import com.mashreq.booking.validation.BookingTimeValidator;
import com.mashreq.booking.validation.MaintenanceTimeValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final MaintenanceTimeValidator maintenanceTimeValidator;
    private final BookingTimeValidator bookingTimeValidator;
    private final BookingsMapper bookingsMapper;

    /**
     *
     * @param requestDto
     * @return
     */
    @Override
    @Transactional
    public BookingResponseDto bookRoom(BookingRequestDto requestDto) {
        log.info("Booking room for {} attendees from {} to {}", requestDto.getAttendees(), requestDto.getStartTime(), requestDto.getEndTime());

        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.parse(requestDto.getStartTime());
        LocalTime endTime = LocalTime.parse(requestDto.getEndTime());

        maintenanceTimeValidator.validate(startTime, endTime);
        bookingTimeValidator.validate(startTime, endTime);

        Optional<ConferenceRoomEntity> availableRoom = findAvailableRooms(startTime, endTime)
                .stream()
                .filter(room -> room.getCapacity() >= requestDto.getAttendees())
                .findFirst();

        if (availableRoom.isPresent()) {
            BookingEntity booking = new BookingEntity();
            booking.setRoomName(availableRoom.get().getName());
            booking.setDate(date);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setAttendees(requestDto.getAttendees());
            log.info("Room {} booked successfully", availableRoom.get().getName());
            BookingEntity save = bookingRepository.save(booking);
            return bookingsMapper.toBookingResponseDto(save, "BOOKED");
        } else {
            log.error("No available room for the specified time and attendees.");
            throw new RoomNotAvailableException("No available room for the specified time and attendees.", "NO_ROOM_AVAILABLE");
        }
    }

    @Override
    public AvailableBookingsResponseDto getAvailableMeetingRooms(LocalDate date, LocalTime startTime, LocalTime endTime) {
        log.debug("Finding available rooms by giving time from {} to {}", startTime, endTime);
        List<ConferenceRoomEntity> availableRooms = findAvailableRooms(startTime, endTime);

        return bookingsMapper.toAvailableBookingsResponseDto(availableRooms);
    }

    private List<ConferenceRoomEntity> findAvailableRooms(LocalTime startTime, LocalTime endTime) {
        log.info("Finding available room from {} to {}", startTime, endTime);
        List<ConferenceRoomEntity> availableRooms = conferenceRoomRepository.findAll()
                .stream()
                .filter(room -> isRoomAvailable(room, startTime, endTime))
                .sorted(Comparator.comparingInt(ConferenceRoomEntity::getCapacity))
                .collect(Collectors.toList());
        log.info("available rooms from {} to {} :: {}", startTime, endTime, availableRooms);
        return availableRooms;
    }

    private boolean isRoomAvailable(ConferenceRoomEntity room, LocalTime startTime, LocalTime endTime) {
        List<BookingEntity> roomBookings = bookingRepository.findByRoomNameAndDate(room.getName(), LocalDate.now());
        return roomBookings.stream().noneMatch(booking ->
                (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime()))
        );
    }

}
