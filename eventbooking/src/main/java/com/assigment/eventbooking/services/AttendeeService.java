package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.Attendee;
import com.assigment.eventbooking.dto.requests.CreateAttendeeRequest;
import com.assigment.eventbooking.dto.requests.ReserveTicketRequest;
import com.assigment.eventbooking.exceptions.AttendeeNotFoundException;
import com.assigment.eventbooking.exceptions.EventDoesNotExistException;

public interface AttendeeService {
    Attendee createAttendee(CreateAttendeeRequest createAttendeeRequest) throws EventDoesNotExistException;
    Attendee findAttendeeByEmail(String email,String eventName) throws AttendeeNotFoundException;
}
