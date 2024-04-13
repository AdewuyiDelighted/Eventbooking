package com.assigment.eventbooking.exceptions;

public class UserHasReservedTicketException extends EventExistException{
    public UserHasReservedTicketException(String message) {
        super(message);
    }
}
