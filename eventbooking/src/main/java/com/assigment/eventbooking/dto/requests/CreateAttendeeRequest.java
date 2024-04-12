package com.assigment.eventbooking.dto.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateAttendeeRequest {
    private String name;
    public String email;
    private String eventName;
}
