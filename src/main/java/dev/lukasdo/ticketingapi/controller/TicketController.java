package dev.lukasdo.ticketingapi.controller;

import dev.lukasdo.ticketingapi.dto.TicketRequest;
import dev.lukasdo.ticketingapi.dto.TicketResponse;
import dev.lukasdo.ticketingapi.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping()
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TicketResponse.fromTicket(ticketService.createTicket(ticketRequest)));
    }
}
