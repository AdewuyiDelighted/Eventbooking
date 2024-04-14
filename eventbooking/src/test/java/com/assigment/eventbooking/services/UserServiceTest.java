package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.BookedEvent;
import com.assigment.eventbooking.data.models.Ticket;
import com.assigment.eventbooking.dto.requests.*;
import com.assigment.eventbooking.dto.responses.CancelReservationResponse;
import com.assigment.eventbooking.dto.responses.CreateEventResponse;
import com.assigment.eventbooking.dto.responses.EventSearchResponse;
import com.assigment.eventbooking.dto.responses.UserRegisterResponse;
import com.assigment.eventbooking.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;

    @Test
    public void testThatUserCanRegister() throws UserAlreadyExistException, InvalidInputException, PasswordTooWeakException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("lope");
        registerRequest.setPassword("Debby@123");
        registerRequest.setEmail("shetr5@gmail.com");
        UserRegisterResponse response = userService.register(registerRequest);
        System.out.println(response.getMessage());
        assertThat(response).isNotNull();
    }
    @Test
    @Sql(scripts = "/scripts/data.sql")
    public void userCanSearchForEvent() throws EventDoesNotExistException, UserDoesntExistException {
        SearchEventRequest searchEventRequest = new SearchEventRequest();
        searchEventRequest.setEventName("SWIT");
        searchEventRequest.setUserEmail("ope11@gmail");
        EventSearchResponse eventSearchResponse = userService.searchForEvent(searchEventRequest);
        assertThat(eventSearchResponse).isNotNull();
    }

    @Test
    @Sql(scripts = "/scripts/data.sql")
    public void userCanReserveTicket() throws UserDoesntExistException, EventDoesNotExistException, NoAvailableSpaceException {
        ReserveTicketRequest reserveTicketRequest = new ReserveTicketRequest();
        reserveTicketRequest.setAttendeeEmail("adeshina1@gmail");
        reserveTicketRequest.setEventName("SWIT");
        Ticket ticket = userService.reserveTicket(reserveTicketRequest);
        assertThat(ticket).isNotNull();
    }

    @Test
    @Sql(scripts = "/scripts/data.sql")
    public void testThatUserCreateEvent() throws UserDoesntExistException, EventCategoryNotAvailableException, EventExistException, InvalidDateFormatException, InvalidDateException, AttendeeCountException, InvalidInputException {
        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setName("Karoke party");
        createEventRequest.setAttendeesCount(30);
        createEventRequest.setDescriptions("");
        createEventRequest.setEventYear(2024);
        createEventRequest.setEventMonth(5);
        createEventRequest.setEventDate(12);
        createEventRequest.setEventType("Game");
        createEventRequest.setUserEmail("ope11@gmail");
        CreateEventResponse createEventResponse = userService.createEvent(createEventRequest);
        assertThat(createEventResponse).isNotNull();
    }


    @Test
    @Sql(scripts = "/scripts/data.sql")
    public void testThatUserCanViewAllBookedEvent() throws EventDoesNotExistException, UserDoesntExistException, NoAvailableSpaceException {
        ReserveTicketRequest reserveTicketRequest = new ReserveTicketRequest();
        reserveTicketRequest.setAttendeeEmail("ope11@gmail");
        reserveTicketRequest.setEventName("SWIT");
        userService.reserveTicket(reserveTicketRequest);
        ReserveTicketRequest reserveTicketRequestTwo = new ReserveTicketRequest();
        reserveTicketRequestTwo.setAttendeeEmail("ope11@gmail");
        reserveTicketRequestTwo.setEventName("houseparty");
        userService.reserveTicket(reserveTicketRequestTwo);
        List<BookedEvent> bookedEvents = userService.viewAllBookEvents("ope11@gmail");
        assertThat(bookedEvents).hasSize(2);
    }

    @Test
    @Sql(scripts = "/scripts/data.sql")
    public void testThatUserCanCancelAReservation() throws EventDoesNotExistException, UserDoesntExistException, EventNotBookedException, OverLappedEventDateException, NoAvailableSpaceException, AttendeeNotFoundException, UserHasReservedTicketException {
        CancelReservationRequest cancelReservationRequest = new CancelReservationRequest();
        ReserveTicketRequest reserveTicketRequest = new ReserveTicketRequest();
        reserveTicketRequest.setAttendeeEmail("adeshina1@gmail");
        reserveTicketRequest.setEventName("houseparty");
        userService.reserveTicket(reserveTicketRequest);
        cancelReservationRequest.setEmail("adeshina1@gmail");
        cancelReservationRequest.setEventName("houseparty");
        CancelReservationResponse cancelReservationResponse = userService.cancelReservation(cancelReservationRequest);
        assertThat(cancelReservationResponse).isNotNull();
    }




}
