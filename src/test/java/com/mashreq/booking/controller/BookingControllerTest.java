package com.mashreq.booking.controller;

import com.mashreq.booking.service.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingServiceImpl bookingServiceImpl;

    @Test
    public void shouldReturnAvailableBookings() throws Exception {
        mockMvc.perform(get("/internal/bookings?startTime=09:30&endTime=10:30"))
                .andExpect(status().isOk());
    }
}
