package com.assigment.eventbooking.data.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class BookedEvent {
    @Id
    @GeneratedValue (strategy = IDENTITY)
    private Long id;
    private String eventName;
    private LocalDate eventDate;
    private Category category;
    @ManyToOne
    private User user;

}
