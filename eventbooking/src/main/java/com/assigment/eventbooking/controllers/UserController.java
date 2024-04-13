package com.assigment.eventbooking.controllers;


import com.assigment.eventbooking.dto.requests.*;
import com.assigment.eventbooking.dto.responses.ApiResponse;
import com.assigment.eventbooking.exceptions.EventBookingExceptions;
import com.assigment.eventbooking.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mep/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody RegisterRequest registerRequest) {
        try {
            return new ResponseEntity<>(userService.register(registerRequest), HttpStatus.CREATED);
        } catch (EventBookingExceptions exceptions) {
            return new ResponseEntity<>(new ApiResponse(false, exceptions.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/createEvent")
    public ResponseEntity<?> createEvent(@RequestBody CreateEventRequest createEventRequest) {
        try {
            return new ResponseEntity<>(userService.createEvent(createEventRequest), HttpStatus.CREATED);
        } catch (EventBookingExceptions exceptions) {
            return new ResponseEntity<>(new ApiResponse(false, exceptions.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/searchForEvent")
    public ResponseEntity<?> searchForEvent(SearchEventRequest searchEventRequest) {
        try {
            return new ResponseEntity<>(userService.searchForEvent(searchEventRequest), HttpStatus.FOUND);
        } catch (EventBookingExceptions exceptions) {
            return new ResponseEntity<>(new ApiResponse(false, exceptions.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/makeReservation")
    public ResponseEntity<?> makeReservation(@RequestBody ReserveTicketRequest reserveTicketRequest) {
        try {
            return new ResponseEntity<>(userService.reserveTicket(reserveTicketRequest), HttpStatus.ACCEPTED);
        } catch (EventBookingExceptions exceptions) {
            return new ResponseEntity<>(new ApiResponse(false, exceptions.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBookedEvents")
    public ResponseEntity<?> getBookedEvents(@RequestParam(name = "email") String email) {
        try {
            return new ResponseEntity<>(userService.viewAllBookEvents(email), HttpStatus.FOUND);
        } catch (EventBookingExceptions exceptions) {
            return new ResponseEntity<>(new ApiResponse(false, exceptions.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cancelReservation")
    public ResponseEntity<?> cancelReservation(CancelReservationRequest cancelReservationRequest) {
        try {
            return new ResponseEntity<>(userService.cancelReservation(cancelReservationRequest), HttpStatus.OK);
        } catch (EventBookingExceptions exceptions) {
            return new ResponseEntity<>(new ApiResponse(false, exceptions.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }


}