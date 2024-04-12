package com.assigment.eventbooking.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateEventRequest {
    private String name;
    private int eventYear;
    private int eventMonth;
    private int eventDate;
    private Integer attendeesCount;
    private String descriptions;
    private String eventType;
    private String userEmail;

}
