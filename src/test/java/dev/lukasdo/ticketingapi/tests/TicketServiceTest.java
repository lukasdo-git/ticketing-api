package dev.lukasdo.ticketingapi.tests;

import dev.lukasdo.ticketingapi.dto.TicketRequest;
import dev.lukasdo.ticketingapi.model.Priority;
import dev.lukasdo.ticketingapi.model.Status;
import dev.lukasdo.ticketingapi.model.Ticket;
import dev.lukasdo.ticketingapi.repository.TicketRepository;
import dev.lukasdo.ticketingapi.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    public void testCreateTicket() {
        //arrange data
        TicketRequest mockRequest = new TicketRequest();
        mockRequest.setTitle("Server is down");
        mockRequest.setDescription("The NAS 'JUPITER' is down, cannot access data...");
        mockRequest.setPriority(Priority.MEDIUM);
        //arrange behaviour
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act
        Ticket result = ticketService.createTicket(mockRequest);

        //assert
        assertThat(result.getTitle()).isEqualTo(mockRequest.getTitle());
        assertThat(result.getDescription()).isEqualTo(mockRequest.getDescription());
        assertThat(result.getPriority()).isEqualTo(mockRequest.getPriority());
        assertThat(result.getStatus()).isEqualTo(Status.SUBMITTED);
        verify(ticketRepository, times(1)).save(any(Ticket.class));

    }
}
