package com.assigment.eventbooking.dto.requests;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReserveTicketRequest {
    private String attendeeEmail;
    private String EventName;
}
