package dev.lukasdo.ticketingapi.repository;

import dev.lukasdo.ticketingapi.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
