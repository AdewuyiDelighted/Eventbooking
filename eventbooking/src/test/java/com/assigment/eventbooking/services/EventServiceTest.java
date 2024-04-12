package com.assigment.eventbooking.services;

import com.assigment.eventbooking.dto.requests.CreateEventRequest;
import com.assigment.eventbooking.dto.requests.EventSearchRequest;
import com.assigment.eventbooking.dto.responses.CreateEventResponse;
import com.assigment.eventbooking.exceptions.EventCategoryNotAvailableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EventServiceTest {
    @Autowired
    private EventService eventService;

    @Test
    public void createEvent() throws EventCategoryNotAvailableException {
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName("SWIT");
        createEventRequest.setAttendeesCount(100);
        createEventRequest.setDescriptions("Event for women in tech");
        createEventRequest.setEventYear(2024);
        createEventRequest.setEventMonth(5);
        createEventRequest.setEventDate(12);
        createEventRequest.setEventType("Conference");
        CreateEventResponse createEventResponse = eventService.createEvent(createEventRequest);
        System.out.println(createEventResponse.getMessage());
        assertThat(createEventResponse).isNotNull();
    }



}
