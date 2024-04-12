package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.Attendee;
import com.assigment.eventbooking.data.repositories.AttendeeRepository;
import com.assigment.eventbooking.dto.requests.CreateAttendeeRequest;
import com.assigment.eventbooking.dto.requests.ReserveTicketRequest;
import com.assigment.eventbooking.exceptions.AttendeeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppAttendeeService implements AttendeeService {
    private final AttendeeRepository attendeeRepository;

    @Override
    public Attendee createAttendee(CreateAttendeeRequest createAttendeeRequest) {
        Attendee attendee = new Attendee();
        attendee.setName(createAttendeeRequest.getName());
        attendee.setEmail(createAttendeeRequest.getEmail());
        attendeeRepository.save(attendee);
        return attendee;
    }

    @Override
    public Attendee findAttendeeByEmail(String email, String eventName) throws AttendeeNotFoundException {
        Attendee attendee = attendeeRepository.findAttendeeByEmail(email);
        if (attendee.getEventName().equals(eventName)) {
            return attendee;
        }
        throw new AttendeeNotFoundException("Sorry Reservation Wasn't Made For This Event");
    }
}
