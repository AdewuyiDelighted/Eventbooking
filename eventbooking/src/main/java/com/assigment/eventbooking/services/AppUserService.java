package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.*;
import com.assigment.eventbooking.data.repositories.BookedEventRepository;
import com.assigment.eventbooking.data.repositories.UserRepository;
import com.assigment.eventbooking.dto.requests.*;
import com.assigment.eventbooking.dto.responses.CancelReservationResponse;
import com.assigment.eventbooking.dto.responses.CreateEventResponse;
import com.assigment.eventbooking.dto.responses.EventSearchResponse;
import com.assigment.eventbooking.dto.responses.UserRegisterResponse;
import com.assigment.eventbooking.exceptions.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AppUserService implements UserService {
    private final EventService eventService;
    private final UserRepository userRepository;
    private final AttendeeService attendeeService;
    private final BookedEventRepository bookedEventRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    @Override
    public UserRegisterResponse register(@Valid RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        userRegisterResponse.setMessage("Registration Successful");
        userRepository.save(user);
        return userRegisterResponse;
    }

    @Override
    public EventSearchResponse searchForEvent(String eventName) throws EventDoesNotExistException {
        return modelMapper.map(eventService.searchAEvent(eventName), EventSearchResponse.class);
    }

    @Override
    public Ticket reserveTicket(ReserveTicketRequest reserveTicketRequest) throws UserDoesntExistException, EventDoesNotExistException, NoAvailableSpaceException {
        User user = findUserByEmail(reserveTicketRequest.getAttendeeEmail());
        addToBookedEvent(user, reserveTicketRequest.getEventName());
        CreateAttendeeRequest createAttendeeRequest = modelMapper.map(user, CreateAttendeeRequest.class);
        createAttendeeRequest.setEventName(reserveTicketRequest.getEventName());
        Attendee attendee = attendeeService.createAttendee(createAttendeeRequest);
        return eventService.reserveTicket(attendee, reserveTicketRequest.getEventName());
    }

    @Override
    public List<BookedEvent> viewAllBookEvents(String email) throws UserDoesntExistException {
        User user = findUserByEmail(email);
        return bookedEventRepository.findBookedEventByUser(user);
    }

    @Override
    public CancelReservationResponse cancelReservation(CancelReservationRequest cancelReservationRequest) throws UserDoesntExistException, EventDoesNotExistException, EventNotBookedException, OverLappedEventDateException, AttendeeNotFoundException {
        User user = findUserByEmail(cancelReservationRequest.getEmail());
        BookedEvent bookedEvent = findABookedEvent(user.getEmail(), cancelReservationRequest.getEventName());
        return removeABookedEvent(bookedEvent, user.getEmail());

    }

    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws EventCategoryNotAvailableException, UserDoesntExistException {
        User user = findUserByEmail(createEventRequest.getUserEmail());
        return eventService.createEvent(createEventRequest);
    }

    @Override
    public User findUserByEmail(String email) throws UserDoesntExistException {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserDoesntExistException("User Doesnt Exist"));
    }

    private void addToBookedEvent(User user, String eventName) throws UserDoesntExistException, EventDoesNotExistException {
        BookedEvent bookedEvent = new BookedEvent();
        Event event = eventService.searchAEvent(eventName);
        bookedEvent.setEventName(event.getName());
        bookedEvent.setEventDate(event.getDate());
        bookedEvent.setUser(user);
        bookedEvent.setCategory(event.getCategory());
        bookedEventRepository.save(bookedEvent);
        System.out.println("Book event saved");
    }

    private BookedEvent findABookedEvent(String userEmail, String eventName) throws EventNotBookedException, UserDoesntExistException {
        for (BookedEvent bookedEvent : viewAllBookEvents(userEmail)) {
            if (bookedEvent.getEventName().equals(eventName)) return bookedEvent;
        }
        throw new EventNotBookedException("Event Doesn't Exist In Your Record");

//      return bookedEventRepository.findByEventName(eventName).orElseThrow(()-> new EventNotBookedException("Event Doesn't Exist In Your Record"))

    }

    private CancelReservationResponse removeABookedEvent(BookedEvent bookedEvent, String userEmail) throws EventDoesNotExistException, OverLappedEventDateException, AttendeeNotFoundException {
        if (bookedEvent.getEventDate().isBefore(LocalDate.now()) || bookedEvent.getEventDate().equals(LocalDate.now())) {
            CancelReservationResponse cancelReservationResponse = new CancelReservationResponse();
            eventService.cancelReservation(attendeeService.findAttendeeByEmail(userEmail, bookedEvent.getEventName()), bookedEvent.getEventName());
            bookedEventRepository.delete(bookedEvent);
            cancelReservationResponse.setMessage("Reservation cancelled");
            return cancelReservationResponse;
        } else throw new OverLappedEventDateException("Event Date Has Elapsed");
    }
}

