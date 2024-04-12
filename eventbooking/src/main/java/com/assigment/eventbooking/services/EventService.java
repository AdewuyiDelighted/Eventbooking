package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.Attendee;
import com.assigment.eventbooking.data.models.Event;
import com.assigment.eventbooking.data.models.Ticket;
import com.assigment.eventbooking.dto.requests.CreateEventRequest;
import com.assigment.eventbooking.dto.requests.ReserveTicketRequest;
import com.assigment.eventbooking.dto.responses.CreateEventResponse;
import com.assigment.eventbooking.dto.responses.EventSearchResponse;
import com.assigment.eventbooking.exceptions.EventCategoryNotAvailableException;
import com.assigment.eventbooking.exceptions.EventDoesNotExistException;
import com.assigment.eventbooking.exceptions.NoAvailableSpaceException;

public interface EventService {

    CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws EventCategoryNotAvailableException;

    Event searchAEvent(String eventName) throws EventDoesNotExistException;
    Event findEventByName(String eventName) throws EventDoesNotExistException;

    Ticket reserveTicket(Attendee attendee, String eventName) throws EventDoesNotExistException, NoAvailableSpaceException;

    void cancelReservation(Attendee attendee,String eventName) throws EventDoesNotExistException;
}
