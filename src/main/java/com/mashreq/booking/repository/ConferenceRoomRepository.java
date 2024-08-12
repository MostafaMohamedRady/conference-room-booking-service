package com.mashreq.booking.repository;

import com.mashreq.booking.entity.ConferenceRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoomEntity, String> {
}
