package dev.lukasdo.ticketingapi.service;

import dev.lukasdo.ticketingapi.dto.TicketRequest;
import dev.lukasdo.ticketingapi.model.Status;
import dev.lukasdo.ticketingapi.model.Ticket;
import dev.lukasdo.ticketingapi.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(TicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(Status.SUBMITTED);
        //ticket.setUserId(); - later, when JWT is implemented for authentication we can grab the user from SecurityContextHolder
        return ticketRepository.save(ticket);
    }

}
