package com.assigment.eventbooking.data.repositories;

import com.assigment.eventbooking.data.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
