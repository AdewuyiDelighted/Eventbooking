package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.*;
import com.assigment.eventbooking.data.repositories.AttendeeRepository;
import com.assigment.eventbooking.data.repositories.EventRepository;
import com.assigment.eventbooking.dto.requests.CreateEventRequest;
import com.assigment.eventbooking.dto.responses.CreateEventResponse;
import com.assigment.eventbooking.exceptions.*;
import com.assigment.eventbooking.utils.Mapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.assigment.eventbooking.utils.Verification.dateChecker;

@Service
@AllArgsConstructor
public class AppEventService implements EventService {
    private final EventRepository eventRepository;
    private final Validator validator;

    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws EventCategoryNotAvailableException, EventExistException, InvalidDateFormatException, InvalidDateException, AttendeeCountException, InvalidInputException {
        if (!isEventCreated(createEventRequest.getName())) {
            CreateEventResponse response = new CreateEventResponse();
            Event event = checkValidInputs(createEventRequest);
            eventRepository.save(event);
            response.setMessage(event.getName() + " Created Successfully And Scheduled For " + event.getDate());
            return response;
        }
        throw new EventExistException(createEventRequest.getName() + " has Already Been Created");
    }


    @Override
    public Event searchAEvent(String eventName) throws EventDoesNotExistException {
        return findEventByName(eventName);
    }

    @Override
    public Event findEventByName(String eventName) throws EventDoesNotExistException {
        Optional<Event> event = Optional.ofNullable(eventRepository.findByName(eventName).orElseThrow(() -> new EventDoesNotExistException("Event Doesn't exist")));
        return event.get();
    }

    @Override
    public Ticket reserveTicket(Attendee attendee, String eventName) throws EventDoesNotExistException, NoAvailableSpaceException {
        Event event = findEventByName(eventName);
        if (event.getCurrentAttendeeCount() != event.getAvailableAttendeesCount() || event.getCurrentAttendeeCount() < 1000) {
            event.setCurrentAttendeeCount(event.getCurrentAttendeeCount() + 1);
            event.getAttendees().add(attendee);
            eventRepository.save(event);
            return Mapper.map(event);
        }
        throw new NoAvailableSpaceException("We Are Sorry No Space Available");
    }

    @Override
    public void cancelReservation(Attendee attendee, String eventName) throws EventDoesNotExistException, UserHasReservedTicketException, AttendeeNotFoundException {
        Event event = checkAttendee(eventName, attendee);
        event.setAttendees(event.getAttendees().stream()
                .filter(foundAttendee -> foundAttendee.equals(attendee))
                .collect(Collectors.toList()));
        event.setCurrentAttendeeCount(event.getCurrentAttendeeCount() - 1);
        eventRepository.save(event);
    }

    private Category getEventCategory(String eventType) throws EventCategoryNotAvailableException {
        if (eventType.equalsIgnoreCase("Conference") || eventType.equalsIgnoreCase("Concert") || eventType.equalsIgnoreCase("Game")) {
            return Category.valueOf(eventType.toUpperCase());
        } else throw new EventCategoryNotAvailableException("Event Category Not Available");
    }

    private boolean isEventCreated(String eventName) throws EventExistException {
        Optional<Event> event = eventRepository.findByName(eventName);
        if (event.isPresent()) throw new EventExistException(eventName + " has Already Been Created");
        return false;
    }

    private Event checkAttendee(String eventName, Attendee attendee) throws EventDoesNotExistException, UserHasReservedTicketException, AttendeeNotFoundException {
        Event event = findEventByName(eventName);
        for (Attendee attendee1 : event.getAttendees()) {
            if (attendee1.getEmail().equals(attendee.getEmail()))
                return event;
        }
        throw new AttendeeNotFoundException("User Did Not Have A Reserved Space");
    }

    private Event checkValidInputs(CreateEventRequest createEventRequest) throws InvalidInputException, InvalidDateFormatException, InvalidDateException, AttendeeCountException, EventCategoryNotAvailableException {
        Event event = Mapper.map(createEventRequest);
        event.setCategory(getEventCategory(createEventRequest.getEventType()));
        Set<ConstraintViolation<Event>> constraintViolations = validator.validate(event);
        if (!constraintViolations.isEmpty())
            throw new InvalidInputException(constraintViolations.stream().findAny().get().getMessage());
        dateChecker(createEventRequest.getEventYear(), createEventRequest.getEventMonth(), createEventRequest.getEventDate());
        return event;
    }
}
