package com.assigment.eventbooking.data.repositories;

import com.assigment.eventbooking.data.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findByName(String eventName);

}
