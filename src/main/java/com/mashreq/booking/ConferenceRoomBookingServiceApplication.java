package com.mashreq.booking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Conference Room Booking", version = "1.0", description = "Banking Application Assignment"))
@SpringBootApplication
public class ConferenceRoomBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceRoomBookingServiceApplication.class, args);
	}

}
