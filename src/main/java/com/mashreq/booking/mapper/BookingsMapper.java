package com.mashreq.booking.mapper;

import com.mashreq.booking.dto.AvailableBookingsResponseDto;
import com.mashreq.booking.dto.BookingResponseDto;
import com.mashreq.booking.entity.BookingEntity;
import com.mashreq.booking.entity.ConferenceRoomEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//to use mapstruct
@Component
public class BookingsMapper {

    public AvailableBookingsResponseDto toAvailableBookingsResponseDto(List<ConferenceRoomEntity> rooms) {
        AvailableBookingsResponseDto responseDto = new AvailableBookingsResponseDto();
        List<AvailableBookingsResponseDto.AvailableRoomDto> roomDtos = rooms.stream()
                .map(this::toAvailableRoomDto)
                .collect(Collectors.toList());
        responseDto.setAvailableRooms(roomDtos);
        return responseDto;
    }

    public AvailableBookingsResponseDto.AvailableRoomDto toAvailableRoomDto(ConferenceRoomEntity room) {
        AvailableBookingsResponseDto.AvailableRoomDto roomDto = new AvailableBookingsResponseDto.AvailableRoomDto();
        roomDto.setRoomName(room.getName());
        roomDto.setMaxCapacity(room.getCapacity());
        return roomDto;
    }


    public BookingResponseDto toBookingResponseDto(BookingEntity booking, String bookingStatus) {
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setBookingReference(String.valueOf(booking.getId()));
        responseDto.setRoomName(booking.getRoomName());
        responseDto.setStartTime(booking.getStartTime().toString());
        responseDto.setEndTime(booking.getEndTime().toString());
        responseDto.setNumberOfPeople(booking.getAttendees());
        responseDto.setStatus(bookingStatus);
        return responseDto;
    }
}
