package com.assigment.eventbooking.utils;

import com.assigment.eventbooking.data.models.*;
import com.assigment.eventbooking.dto.requests.CreateAttendeeRequest;
import com.assigment.eventbooking.dto.requests.CreateEventRequest;
import com.assigment.eventbooking.dto.requests.RegisterRequest;
import com.assigment.eventbooking.exceptions.AttendeeCountException;

import java.time.LocalDate;

public class Mapper {
    public static User map(RegisterRequest registerRequest){
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return user;

    }

    public static Event map(CreateEventRequest createEventRequest) throws AttendeeCountException {
        if(createEventRequest.getAttendeesCount() > 1000)throw new AttendeeCountException("Attendee Can't Exceed 1000");
        Event event = new Event();
        event.setName(createEventRequest.getName());
        event.setAvailableAttendeesCount(createEventRequest.getAttendeesCount());
        event.setEventDescription(createEventRequest.getDescriptions());
        event.setDate(LocalDate.of(createEventRequest.getEventYear(), createEventRequest.getEventMonth(), createEventRequest.getEventDate()));
        return event;

    }
    public static Ticket map(Event event){
        Ticket ticket = new Ticket();
        ticket.setEventName(event.getName());
        ticket.setEventDate(event.getDate());
        ticket.setTicketNumber(event.getCurrentAttendeeCount());
        return ticket;
    }
    public static BookedEvent map(Event event,User user){
        BookedEvent bookedEvent = new BookedEvent();
        bookedEvent.setEventName(event.getName());
        bookedEvent.setEventDate(event.getDate());
        bookedEvent.setUser(user);
        bookedEvent.setCategory(event.getCategory());
        return bookedEvent;
    }
    public static Attendee map(CreateAttendeeRequest createAttendeeRequest){
        Attendee attendee = new Attendee();
        attendee.setName(createAttendeeRequest.getName());
        attendee.setEmail(createAttendeeRequest.getEmail());
        attendee.setEventName(createAttendeeRequest.getEventName());
        return attendee;
    }
}
