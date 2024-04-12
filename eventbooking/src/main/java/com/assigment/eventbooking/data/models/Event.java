package com.assigment.eventbooking.data.models;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private LocalDate date;

    private int availableAttendeesCount;

    private String eventDescription;

    private int currentAttendeeCount = 0;

    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE})
    private List<Attendee> attendees;

    private Category category;

    private String eventAnchorName;

}
