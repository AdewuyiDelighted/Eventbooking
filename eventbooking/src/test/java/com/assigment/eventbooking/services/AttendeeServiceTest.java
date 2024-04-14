package com.assigment.eventbooking.services;

import com.assigment.eventbooking.data.models.Attendee;
import com.assigment.eventbooking.data.models.User;
import com.assigment.eventbooking.dto.requests.CreateAttendeeRequest;
import com.assigment.eventbooking.exceptions.EventDoesNotExistException;
import com.assigment.eventbooking.exceptions.UserDoesntExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AttendeeServiceTest {
    @Autowired
    AttendeeService attendeeService;
    @Autowired
    UserService userService;



    @Test
    @Sql(scripts = "/scripts/data.sql")
    public void testCreateAttendee() throws UserDoesntExistException, EventDoesNotExistException {
        CreateAttendeeRequest createAttendeeRequest = new CreateAttendeeRequest();
        User user = userService.findUserByEmail("ope11@gmail");
        createAttendeeRequest.setName(user.getName());
        createAttendeeRequest.setEmail(user.getEmail());
        Attendee attendee = attendeeService.createAttendee(createAttendeeRequest);
        assertThat(attendee).isNotNull();
    }

}
