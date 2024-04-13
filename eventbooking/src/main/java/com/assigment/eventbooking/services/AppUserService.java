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
import com.assigment.eventbooking.utils.Mapper;
import com.assigment.eventbooking.utils.Verification;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AppUserService implements UserService {
    private final EventService eventService;
    private final UserRepository userRepository;
    private final AttendeeService attendeeService;
    private final BookedEventRepository bookedEventRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    private final Validator validator;

    @Override
    public UserRegisterResponse register(RegisterRequest registerRequest) throws UserAlreadyExistException, InvalidInputException, PasswordTooWeakException {
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        Verification.passwordChecker(registerRequest.getPassword());
        bcrypt(registerRequest);
        if (!userExist(registerRequest.getEmail())) {
            User user = modelMapper.map(registerRequest,User.class);
            Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
            if (!constraintViolations.isEmpty()) throw new InvalidInputException(constraintViolations.stream().findAny().get().getMessage());
            userRepository.save(user);
            userRegisterResponse.setMessage("Registration Successful");
            return userRegisterResponse;
        }
        throw new UserAlreadyExistException("User already exist");
    }


    @Override
    public EventSearchResponse searchForEvent(SearchEventRequest searchEventRequest) throws EventDoesNotExistException, UserDoesntExistException {
        User user = findUserByEmail(searchEventRequest.getUserEmail());
        return modelMapper.map(eventService.searchAEvent(searchEventRequest.getEventName()), EventSearchResponse.class);
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
    public CancelReservationResponse cancelReservation(CancelReservationRequest cancelReservationRequest) throws UserDoesntExistException, EventDoesNotExistException, EventNotBookedException, OverLappedEventDateException, AttendeeNotFoundException, UserHasReservedTicketException {
        User user = findUserByEmail(cancelReservationRequest.getEmail());
        BookedEvent bookedEvent = findABookedEvent(user.getEmail(), cancelReservationRequest.getEventName());
        return removeABookedEvent(bookedEvent, user.getEmail());

    }

    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws EventCategoryNotAvailableException, UserDoesntExistException, EventExistException, InvalidDateFormatException, InvalidDateException, AttendeeCountException, InvalidInputException {
        User user = findUserByEmail(createEventRequest.getUserEmail());
        return eventService.createEvent(createEventRequest);
    }

    @Override
    public User findUserByEmail(String email) throws UserDoesntExistException {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserDoesntExistException("User Doesnt Exist"));
    }

    private void addToBookedEvent(User user, String eventName) throws UserDoesntExistException, EventDoesNotExistException {
        Event event = eventService.searchAEvent(eventName);
        BookedEvent bookedEvent = Mapper.map(event, user);
        bookedEventRepository.save(bookedEvent);
    }

    private BookedEvent findABookedEvent(String userEmail, String eventName) throws EventNotBookedException, UserDoesntExistException {
        for (BookedEvent bookedEvent : viewAllBookEvents(userEmail)) {
            if (bookedEvent.getEventName().equals(eventName)) return bookedEvent;
        }
        throw new EventNotBookedException("Event Doesn't Exist In Your Record");
    }

    private CancelReservationResponse removeABookedEvent(BookedEvent bookedEvent, String userEmail) throws EventDoesNotExistException, OverLappedEventDateException, AttendeeNotFoundException, UserHasReservedTicketException {
        if (bookedEvent.getEventDate().isBefore(LocalDate.now()) || bookedEvent.getEventDate().equals(LocalDate.now())) {
            CancelReservationResponse cancelReservationResponse = new CancelReservationResponse();
            eventService.cancelReservation(attendeeService.findAttendeeByEmail(userEmail, bookedEvent.getEventName()), bookedEvent.getEventName());
            bookedEventRepository.delete(bookedEvent);
            cancelReservationResponse.setMessage("Reservation cancelled");
            return cancelReservationResponse;
        } else throw new OverLappedEventDateException("Event Date Has Elapsed");
    }

    private boolean userExist(String email) throws UserAlreadyExistException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) throw new UserAlreadyExistException("User Already Exist");
        return false;
    }

    public static void bcrypt(RegisterRequest registerRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(encodedPassword);
    }
}

