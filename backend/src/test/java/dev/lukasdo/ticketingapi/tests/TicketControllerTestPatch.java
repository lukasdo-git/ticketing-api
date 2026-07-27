package dev.lukasdo.ticketingapi.tests;

import dev.lukasdo.ticketingapi.controller.TicketController;
import dev.lukasdo.ticketingapi.dto.StatusUpdateRequest;
import dev.lukasdo.ticketingapi.exception.TicketNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTestPatch {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper serializer;

    @MockitoBean
    TicketService ticketService;

    @Test
    public void updateTicket_returnsNotFound_whenIdNotFound() throws Exception {
        //arrange
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setStatus(Status.IN_REVIEW);
        when(ticketService.updateTicket(any(Long.class), any(StatusUpdateRequest.class))).thenThrow(new TicketNotFoundException(999L));

        //act
        ResultActions response = mockMvc.perform(
                patch("/tickets/999/status")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(statusUpdateRequest))
        );

        //assert
        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Ticket with id 999 not found"));
    }

    @Test
    public void updateTicket_returnsUpdated() throws Exception {
        //arrange
        StatusUpdateRequest statusUpdateRequest = new StatusUpdateRequest();
        statusUpdateRequest.setStatus(Status.RESOLVED);
        Ticket mock = new Ticket();
        mock.setTicketId(1L);
        mock.setStatus(Status.RESOLVED);
        when(ticketService.updateTicket(any(Long.class), any(StatusUpdateRequest.class))).thenReturn(mock);

        //act
        ResultActions response = mockMvc.perform(
                patch("/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(statusUpdateRequest))
        );

        //assert
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(1))
                .andExpect(jsonPath("$.status").value(Status.RESOLVED.toString()));
    }
}
