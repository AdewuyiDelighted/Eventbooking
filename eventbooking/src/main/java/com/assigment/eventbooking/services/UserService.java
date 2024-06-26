package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.BookedEvent;
import com.assigment.eventbooking.data.models.Ticket;
import com.assigment.eventbooking.data.models.User;
import com.assigment.eventbooking.dto.requests.*;
import com.assigment.eventbooking.dto.responses.CancelReservationResponse;
import com.assigment.eventbooking.dto.responses.CreateEventResponse;
import com.assigment.eventbooking.dto.responses.EventSearchResponse;
import com.assigment.eventbooking.dto.responses.UserRegisterResponse;
import com.assigment.eventbooking.exceptions.*;

import java.net.URI;
import java.util.List;

public interface UserService {

    UserRegisterResponse register(RegisterRequest registerRequest) throws UserAlreadyExistException, InvalidInputException, PasswordTooWeakException;

    EventSearchResponse searchForEvent(SearchEventRequest searchEventRequest) throws EventDoesNotExistException, UserDoesntExistException;

    User findUserByEmail(String email) throws UserDoesntExistException;

    Ticket reserveTicket(ReserveTicketRequest reserveTicketRequest) throws UserDoesntExistException, EventDoesNotExistException, NoAvailableSpaceException;

    List<BookedEvent> viewAllBookEvents(String email) throws UserDoesntExistException;

    CancelReservationResponse cancelReservation(CancelReservationRequest cancelReservationRequest) throws UserDoesntExistException, EventDoesNotExistException, EventNotBookedException, OverLappedEventDateException, AttendeeNotFoundException, UserHasReservedTicketException;


    CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws EventCategoryNotAvailableException, UserDoesntExistException, EventExistException, InvalidDateFormatException, InvalidDateException, AttendeeCountException, InvalidInputException;
}

