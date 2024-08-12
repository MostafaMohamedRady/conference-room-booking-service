package com.mashreq.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "conference_room")
public class ConferenceRoomEntity {
    @Id
    private String name;
    private int capacity;
}
