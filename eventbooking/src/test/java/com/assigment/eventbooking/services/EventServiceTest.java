package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.Attendee;
import com.assigment.eventbooking.data.models.Event;
import com.assigment.eventbooking.data.models.Ticket;
import com.assigment.eventbooking.data.repositories.AttendeeRepository;
import com.assigment.eventbooking.data.repositories.EventRepository;
import com.assigment.eventbooking.dto.requests.CreateAttendeeRequest;
import com.assigment.eventbooking.dto.requests.CreateEventRequest;
import com.assigment.eventbooking.dto.requests.EventSearchRequest;
import com.assigment.eventbooking.dto.requests.ReserveTicketRequest;
import com.assigment.eventbooking.dto.responses.CreateEventResponse;
import com.assigment.eventbooking.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class EventServiceTest {
    @Autowired
    private EventService eventService;
    @Autowired
    private AttendeeService attendeeService;
    @Autowired
    private AttendeeRepository attendeeRepository;
    @Autowired
    private EventRepository eventRepository;


    @AfterEach
    public void setUp() {
        eventRepository.deleteAll();
        attendeeRepository.deleteAll();



    }

    @Test
    public void createEvent() throws EventCategoryNotAvailableException, InvalidDateFormatException, InvalidDateException, EventExistException, AttendeeCountException, InvalidInputException {
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

    @Test
    public void testWhenUserEnterInvalidDate() throws EventCategoryNotAvailableException, InvalidDateFormatException, InvalidDateException, EventExistException, AttendeeCountException, InvalidInputException {
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName("SWIT");
        createEventRequest.setAttendeesCount(100);
        createEventRequest.setDescriptions("Event for women in tech");
        createEventRequest.setEventYear(2023);
        createEventRequest.setEventMonth(5);
        createEventRequest.setEventDate(12);
        createEventRequest.setEventType("Conference");
        assertThrows(InvalidDateException.class, () -> eventService.createEvent(createEventRequest));

    }

    @Test
    @Sql(scripts = "/scripts/data.sql")
    public void
    testWhenUserGetTicketWhenTheyReserveSpace() throws AttendeeNotFoundException, EventDoesNotExistException, NoAvailableSpaceException {
        Attendee attendee = attendeeService.findAttendeeByEmail("Layo13@gmail", "SWIT");
        Ticket ticket = eventService.reserveTicket(attendee, "SWIT");
        assertEquals("SWIT", ticket.getEventName());
    }

    @Test
    @Sql(scripts = "/scripts/data.sql")
    public void testUserCanCancelTicket() throws AttendeeNotFoundException, EventDoesNotExistException, NoAvailableSpaceException, UserHasReservedTicketException {
        Event event = eventService.findEventByName("SWIT");
        CreateAttendeeRequest createAttendeeRequest = new CreateAttendeeRequest();
        createAttendeeRequest.setEmail("ope11@gmail");
        createAttendeeRequest.setEventName("SWIT");
        createAttendeeRequest.setName("Ope");
        Attendee attendee = attendeeService.createAttendee(createAttendeeRequest);
        eventService.reserveTicket(attendee, event.getName());
        eventService.cancelReservation(attendee, "SWIT");
        assertThrows(AttendeeNotFoundException.class, () -> attendeeService.findAttendeeByEmail(attendee.getEmail(), "SWIT"));
    }


}
