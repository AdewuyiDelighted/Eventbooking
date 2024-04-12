package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.Attendee;
import com.assigment.eventbooking.dto.requests.CreateAttendeeRequest;
import com.assigment.eventbooking.dto.requests.ReserveTicketRequest;
import com.assigment.eventbooking.exceptions.AttendeeNotFoundException;

public interface AttendeeService {
    Attendee createAttendee(CreateAttendeeRequest createAttendeeRequest);
    Attendee findAttendeeByEmail(String email,String eventName) throws AttendeeNotFoundException;
}
