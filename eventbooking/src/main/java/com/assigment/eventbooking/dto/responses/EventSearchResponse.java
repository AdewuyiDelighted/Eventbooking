package com.assigment.eventbooking.dto.responses;

import com.assigment.eventbooking.data.models.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventSearchResponse {
    private String name;
    private LocalDate date;
    private Integer availableAttendeesCount;
    private String eventDescription;
    private Category category;
}
