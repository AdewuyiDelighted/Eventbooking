package com.assigment.eventbooking.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String eventName;

    private LocalDate eventDate;

    private int ticketNumber;
}
