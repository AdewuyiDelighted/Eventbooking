package com.assigment.eventbooking.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchEventRequest {
    private String userEmail;
    private String eventName;
}
