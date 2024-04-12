package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.Attendee;
import com.assigment.eventbooking.data.models.Category;
import com.assigment.eventbooking.data.models.Event;
import com.assigment.eventbooking.data.models.Ticket;
import com.assigment.eventbooking.data.repositories.EventRepository;
import com.assigment.eventbooking.dto.requests.CreateEventRequest;
import com.assigment.eventbooking.dto.responses.CreateEventResponse;
import com.assigment.eventbooking.exceptions.EventCategoryNotAvailableException;
import com.assigment.eventbooking.exceptions.EventDoesNotExistException;
import com.assigment.eventbooking.exceptions.NoAvailableSpaceException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppEventService implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    @Override
    public CreateEventResponse createEvent(CreateEventRequest createEventRequest) throws EventCategoryNotAvailableException {
        Event event = new Event();
        event.setName(createEventRequest.getName());
        event.setAvailableAttendeesCount(createEventRequest.getAttendeesCount());
        event.setEventDescription(createEventRequest.getDescriptions());
        event.setDate(LocalDate.of(createEventRequest.getEventYear(), createEventRequest.getEventMonth(), createEventRequest.getEventDate()));
        event.setCategory(getEventCategory(createEventRequest.getEventType()));
        CreateEventResponse response = new CreateEventResponse();
        response.setMessage(event.getName() + " Created Successfully And Scheduled For " + event.getDate());
        eventRepository.save(event);
        return response;
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
        if (event.getCurrentAttendeeCount() != event.getAvailableAttendeesCount()) {
            event.setCurrentAttendeeCount(event.getCurrentAttendeeCount() + 1);
            event.getAttendees().add(attendee);
            eventRepository.save(event);
            Ticket ticket = new Ticket();
            ticket.setEventName(event.getName());
            ticket.setEventDate(event.getDate());
            ticket.setTicketNumber(event.getCurrentAttendeeCount());
            return ticket;
        }
        throw new NoAvailableSpaceException("We Are Sorry No Space Available");
    }

    @Override
    public void cancelReservation(Attendee attendee, String eventName) throws EventDoesNotExistException {
        Event event = findEventByName(eventName);
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
}
