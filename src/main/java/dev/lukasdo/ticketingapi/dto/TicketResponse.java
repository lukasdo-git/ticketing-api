package dev.lukasdo.ticketingapi.dto;

import dev.lukasdo.ticketingapi.model.Priority;
import dev.lukasdo.ticketingapi.model.Status;
import dev.lukasdo.ticketingapi.model.Ticket;

public class TicketResponse {
    private Long ticketId;
    private String title;
    private String description;
    private Status status;
    private Priority priority;

    public static TicketResponse fromTicket(Ticket ticket) {
        TicketResponse res = new TicketResponse();
        res.ticketId = ticket.getTicketId();
        res.title = ticket.getTitle();
        res.description = ticket.getDescription();
        res.status = ticket.getStatus();
        res.priority = ticket.getPriority();
        return res;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }
}
