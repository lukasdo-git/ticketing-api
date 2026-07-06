package dev.lukasdo.ticketingapi.tests;

import dev.lukasdo.ticketingapi.controller.TicketController;
import dev.lukasdo.ticketingapi.dto.TicketRequest;
import dev.lukasdo.ticketingapi.model.Priority;
import dev.lukasdo.ticketingapi.model.Status;
import dev.lukasdo.ticketingapi.model.Ticket;
import dev.lukasdo.ticketingapi.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTestCreateTicket {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper serializer;

    @MockitoBean
    TicketService ticketService;

    @Test
    public void testCreateTicket() throws Exception {
        //arrange data
        TicketRequest mockRequest = new TicketRequest();
        mockRequest.setTitle("Server is down");
        mockRequest.setDescription("The NAS 'JUPITER' is down, cannot access data...");
        mockRequest.setPriority(Priority.MEDIUM);
        //arrange behaviour
        Ticket mockTicket = new Ticket();
        mockTicket.setTicketId(1L);
        mockTicket.setTitle(mockRequest.getTitle());
        mockTicket.setDescription(mockRequest.getDescription());
        mockTicket.setPriority(mockRequest.getPriority());
        mockTicket.setStatus(Status.SUBMITTED);
        when(ticketService.createTicket(any(TicketRequest.class))).thenReturn(mockTicket);

        //act
        ResultActions response = mockMvc.perform(
                post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializer.writeValueAsString(mockRequest))
        );

        //assert
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value(1))
                .andExpect(jsonPath("$.title").value(mockRequest.getTitle()))
                .andExpect(jsonPath("$.description").value(mockRequest.getDescription()))
                .andExpect(jsonPath("$.priority").value(mockRequest.getPriority().toString()))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

}
