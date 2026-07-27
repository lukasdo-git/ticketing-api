package dev.lukasdo.ticketingapi.tests;

import dev.lukasdo.ticketingapi.controller.TicketController;
import dev.lukasdo.ticketingapi.exception.TicketNotFoundException;
import dev.lukasdo.ticketingapi.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTestDelete {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TicketService ticketService;

    @Test
    public void deleteTicket_returnsNotFound_whenIdNotFound() throws Exception {
        //arrange
        doThrow(new TicketNotFoundException(999L)).when(ticketService).deleteTicket(999L);

        //act
        ResultActions response = mockMvc.perform(
                delete("/tickets/999")
        );

        //assert
        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Ticket with id 999 not found"));
    }

    @Test
    public void deleteTicket_returns204_whenIdExists() throws Exception {
        //arrange
        //nothing to arrange, the happy path is through a void method.

        //act
        ResultActions response = mockMvc.perform(
                delete("/tickets/999")
        );

        //assert
        response
                .andExpect(status().isNoContent());
    }
}
