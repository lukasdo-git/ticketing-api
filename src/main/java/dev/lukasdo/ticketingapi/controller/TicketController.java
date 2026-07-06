package dev.lukasdo.ticketingapi.controller;

import dev.lukasdo.ticketingapi.dto.TicketRequest;
import dev.lukasdo.ticketingapi.dto.TicketResponse;
import dev.lukasdo.ticketingapi.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        List<TicketResponse> responses = ticketService.getAllTickets().stream()
                .map(TicketResponse::fromTicket)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable("ticketId") Long id) {
        return ResponseEntity.ok(TicketResponse.fromTicket(ticketService.getTicketById(id)));
    }
}
