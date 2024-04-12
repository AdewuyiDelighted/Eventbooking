package com.assigment.eventbooking.dto.requests;

import com.assigment.eventbooking.exceptions.EventBookingExceptions;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancelReservationRequest  {
    private String email;
    private String eventName;
}
