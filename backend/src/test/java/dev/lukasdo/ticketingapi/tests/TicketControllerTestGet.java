package dev.lukasdo.ticketingapi.tests;

import dev.lukasdo.ticketingapi.controller.TicketController;
import dev.lukasdo.ticketingapi.exception.TicketNotFoundException;
import dev.lukasdo.ticketingapi.model.Priority;
import dev.lukasdo.ticketingapi.model.Status;
import dev.lukasdo.ticketingapi.model.Ticket;
import dev.lukasdo.ticketingapi.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTestGet {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TicketService ticketService;

    @Test
    public void getAllTickets_returnsListOfTickets() throws Exception {
        //arrange
        Ticket mock1 = new Ticket();
        mock1.setTicketId(1L);
        mock1.setTitle("Mock Title1");
        mock1.setDescription("Mock Description1");
        mock1.setStatus(Status.IN_REVIEW);
        mock1.setPriority(Priority.HIGH);

        Ticket mock2 = new Ticket();
        mock2.setTicketId(2L);
        mock2.setTitle("Mock Title2");
        mock2.setDescription("Mock Description2");
        mock2.setStatus(Status.RESOLVED);
        mock2.setPriority(Priority.LOW);

        when(ticketService.getAllTickets()).thenReturn(List.of(mock1, mock2));

        //act
        ResultActions response = mockMvc.perform(
                get("/tickets")
        );

        //assert
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].ticketId").value(1))
                .andExpect(jsonPath("$[0].title").value("Mock Title1"))
                .andExpect(jsonPath("$[0].description").value("Mock Description1"))
                .andExpect(jsonPath("$[0].status").value(Status.IN_REVIEW.toString()))
                .andExpect(jsonPath("$[0].priority").value(Priority.HIGH.toString()))
                .andExpect(jsonPath("$[1].ticketId").value(2))
                .andExpect(jsonPath("$[1].title").value("Mock Title2"))
                .andExpect(jsonPath("$[1].description").value("Mock Description2"))
                .andExpect(jsonPath("$[1].status").value(Status.RESOLVED.toString()))
                .andExpect(jsonPath("$[1].priority").value(Priority.LOW.toString()));

    }

    @Test
    public void getSpecificTicket_returnsNotFound_whenIdNotFound() throws Exception {
        //arrange
        when(ticketService.getTicketById(999L)).thenThrow(new TicketNotFoundException(999L));

        //act
        ResultActions response = mockMvc.perform(
                get("/tickets/999")
        );

        //assert
        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Ticket with id 999 not found"));
    }

    @Test
    public void getSpecificTicket_returnsTicket_whenFound() throws Exception {
        //arrange
        Ticket mock1 = new Ticket();
        mock1.setTicketId(1L);
        mock1.setTitle("Mock Title1");
        mock1.setDescription("Mock Description1");
        mock1.setStatus(Status.IN_REVIEW);
        mock1.setPriority(Priority.HIGH);

        when(ticketService.getTicketById(1L)).thenReturn(mock1);

        //act
        ResultActions response = mockMvc.perform(
                get("/tickets/1")
        );

        //assert
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(1))
                .andExpect(jsonPath("$.title").value("Mock Title1"))
                .andExpect(jsonPath("$.description").value("Mock Description1"))
                .andExpect(jsonPath("$.status").value(Status.IN_REVIEW.toString()))
                .andExpect(jsonPath("$.priority").value(Priority.HIGH.toString()));
    }
}
