package com.mashreq.booking.service;

import com.mashreq.booking.dto.AvailableBookingsResponseDto;
import com.mashreq.booking.dto.BookingRequestDto;
import com.mashreq.booking.dto.BookingResponseDto;
import com.mashreq.booking.entity.BookingEntity;
import com.mashreq.booking.entity.ConferenceRoomEntity;
import com.mashreq.booking.exception.InvalidBookingRequestException;
import com.mashreq.booking.exception.RoomNotAvailableException;
import com.mashreq.booking.mapper.BookingsMapper;
import com.mashreq.booking.repository.BookingRepository;
import com.mashreq.booking.repository.ConferenceRoomRepository;
import com.mashreq.booking.validation.BookingTimeValidator;
import com.mashreq.booking.validation.MaintenanceTimeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Spy
    private MaintenanceTimeValidator maintenanceTimeValidator;
    @Spy
    private BookingTimeValidator bookingTimeValidator;
    @Spy
    private BookingsMapper bookingsMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void testBookRoom_Success() {
        when(conferenceRoomRepository.findAll()).thenReturn(roomList());
        when(bookingRepository.findByRoomNameAndDate(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(Collections.emptyList());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setRoomName("Amaze");
        bookingEntity.setDate(LocalDate.now());
        bookingEntity.setStartTime(LocalTime.of(10, 0));
        bookingEntity.setEndTime(LocalTime.of(10, 15));
        bookingEntity.setAttendees(3);
        when(bookingRepository.save(ArgumentMatchers.any(BookingEntity.class))).thenReturn(bookingEntity);


        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .attendees(1)
                .startTime("00:00")
                .endTime("00:15")
                .build();
        BookingResponseDto bookingResponseDto = bookingService.bookRoom(bookingRequestDto);

        Assertions.assertNotNull(bookingResponseDto);
        Assertions.assertEquals("Amaze", bookingResponseDto.getRoomName());
    }

    @Test
    void testBookRoom_RoomNotAvailable() {
        ConferenceRoomEntity amaze = new ConferenceRoomEntity();
        amaze.setName("Amaze");
        amaze.setCapacity(3);

        when(conferenceRoomRepository.findAll()).thenReturn(List.of(amaze));

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setRoomName("Amaze");
        bookingEntity.setDate(LocalDate.now());
        bookingEntity.setStartTime(LocalTime.of(10, 0));
        bookingEntity.setEndTime(LocalTime.of(10, 15));
        bookingEntity.setAttendees(3);
        when(bookingRepository.findByRoomNameAndDate(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(Collections.singletonList(bookingEntity));

        Assertions.assertThrows(RoomNotAvailableException.class, () -> {
            BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                    .attendees(1)
                    .startTime("10:00")
                    .endTime("10:15")
                    .build();

            bookingService.bookRoom(bookingRequestDto);
        });
    }

    @Test
    void testBookRoom_MaintenanceTime() {
        InvalidBookingRequestException invalidBookingRequestException = Assertions.assertThrows(InvalidBookingRequestException.class, () -> {
            BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                    .attendees(1)
                    .startTime("09:00")
                    .endTime("09:15")
                    .build();

            bookingService.bookRoom(bookingRequestDto);
        });

        Assertions.assertEquals("Booking cannot be done during maintenance time.", invalidBookingRequestException.getMessage());
    }

    @Test
    void testGetAvailableMeetingRooms() {
        when(conferenceRoomRepository.findAll()).thenReturn(roomList());
        when(bookingRepository.findByRoomNameAndDate(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(Collections.emptyList());

        AvailableBookingsResponseDto responseDto = bookingService.getAvailableMeetingRooms(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(10, 15));
        Assertions.assertNotNull(responseDto);
        Assertions.assertFalse(responseDto.getAvailableRooms().isEmpty());
        Assertions.assertEquals("Amaze", responseDto.getAvailableRooms().get(0).getRoomName());
    }

    @Test
    void testGetAvailableMeetingRooms_NoRoomsAvailable() {
        when(conferenceRoomRepository.findAll()).thenReturn(roomList());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setRoomName("Amaze");
        bookingEntity.setDate(LocalDate.now());
        bookingEntity.setStartTime(LocalTime.of(10, 0));
        bookingEntity.setEndTime(LocalTime.of(10, 15));
        bookingEntity.setAttendees(3);
        when(bookingRepository.findByRoomNameAndDate(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(LocalDate.class)
        )).thenReturn(Collections.singletonList(bookingEntity));

        AvailableBookingsResponseDto responseDto = bookingService.getAvailableMeetingRooms(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(10, 15));
        Assertions.assertNotNull(responseDto);
        Assertions.assertTrue(responseDto.getAvailableRooms().isEmpty());
    }

    List<ConferenceRoomEntity> roomList() {
        ConferenceRoomEntity amaze = new ConferenceRoomEntity();
        amaze.setName("Amaze");
        amaze.setCapacity(3);

        ConferenceRoomEntity beauty = new ConferenceRoomEntity();
        beauty.setName("Beauty");
        beauty.setCapacity(7);

        ConferenceRoomEntity inspire = new ConferenceRoomEntity();
        inspire.setName("Inspire");
        inspire.setCapacity(12);

        ConferenceRoomEntity strive = new ConferenceRoomEntity();
        strive.setName("Strive");
        strive.setCapacity(20);

        return  Arrays.asList(amaze, beauty, inspire, strive);
    }
}