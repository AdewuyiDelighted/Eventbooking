package com.assigment.eventbooking.data.repositories;

import com.assigment.eventbooking.data.models.BookedEvent;
import com.assigment.eventbooking.data.models.User;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookedEventRepository extends JpaRepository<BookedEvent,Long> {
    List<BookedEvent> findBookedEventByUser(User user);
    Optional<BookedEvent> findByEventName(String eventName);
}
