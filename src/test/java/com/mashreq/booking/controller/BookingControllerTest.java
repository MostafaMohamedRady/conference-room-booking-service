package com.mashreq.booking.controller;

import com.mashreq.booking.dto.AvailableBookingsResponseDto;
import com.mashreq.booking.dto.BookingRequestDto;
import com.mashreq.booking.dto.BookingResponseDto;
import com.mashreq.booking.service.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingServiceImpl bookingService;

    private BookingRequestDto bookingRequestDto;
    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    void setUp() {
        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setAttendees(5);
        bookingRequestDto.setStartTime("10:00");
        bookingRequestDto.setEndTime("11:00");

        bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setBookingReference("1");
        bookingResponseDto.setRoomName("Inspire");
        bookingResponseDto.setStartTime("10:00");
        bookingResponseDto.setEndTime("11:00");
        bookingResponseDto.setNumberOfPeople(5);
        bookingResponseDto.setStatus("BOOKED");
    }

    @Test
    void testBookRoom() throws Exception {
        Mockito.when(bookingService.bookRoom(any(BookingRequestDto.class))).thenReturn(bookingResponseDto);

        mockMvc.perform(post("/internal/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomName\":\"Inspire\",\"attendees\":5,\"startTime\":\"10:00\",\"endTime\":\"11:00\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"bookingReference\":\"" + bookingResponseDto.getBookingReference() + "\",\"roomName\":\"Inspire\",\"startTime\":\"10:00\",\"endTime\":\"11:00\",\"numberOfPeople\":5,\"status\":\"BOOKED\"}"))
                .andDo(print());
    }

    @Test
    void testGetAvailableMeetingRooms() throws Exception {
        AvailableBookingsResponseDto availableBookingsResponseDto = new AvailableBookingsResponseDto();
        Mockito.when(bookingService.getAvailableMeetingRooms(any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(availableBookingsResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/internal/v1/bookings/available-rooms")
                        .param("startTime", "10:00")
                        .param("endTime", "11:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}
