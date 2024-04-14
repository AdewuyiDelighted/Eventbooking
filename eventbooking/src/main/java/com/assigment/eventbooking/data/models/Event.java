package com.assigment.eventbooking.data.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.action.internal.OrphanRemovalAction;
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

    @Size(min=3,max=100 ,message = "Inputted Can Exceed 500 Character")

    private String name;

    private LocalDate date;

   @Max(value = 1000,message = "Number Of Attendee Can't Exceed 1000")
   private int availableAttendeesCount;

    @Size(max=500 ,message = "Inputted Can't Exceed 500 Character")
    private String eventDescription;

    private int currentAttendeeCount = 0;

    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.DETACH},orphanRemoval = true)
    private List<Attendee> attendees;


    private Category category;

    private String eventAnchorName;

}
