package com.assigment.eventbooking.data.repositories;

import com.assigment.eventbooking.data.models.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee,Long> {
    Attendee findAttendeeByEmail(String email);

}
