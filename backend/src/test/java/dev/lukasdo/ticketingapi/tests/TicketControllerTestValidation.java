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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTestValidation {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper serializer;

    @MockitoBean
    TicketService ticketService;


    @Test
    public void createTicket_returnsBadRequest_whenTitleIsBlank() throws Exception {
        //arrange
        TicketRequest mockRequest = new TicketRequest();
        mockRequest.setPriority(Priority.HIGH);

        //act
        ResultActions response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockRequest))
        );

        //assert
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be blank"));

        verify(ticketService, never()).createTicket(any());
    }

    @Test
    public void createTicket_returnsBadRequest_whenTitleIsTooLong() throws Exception {
        //arrange
        TicketRequest mockRequest = new TicketRequest();
        mockRequest.setTitle("Too Long Title Lorem Ipsum Dolor Sit Amet Ameno Dorime");

        //act
        ResultActions response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockRequest))
        );

        //assert
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be more than 50 characers"))
        ;

        verify(ticketService, never()).createTicket(any());
    }

    @Test
    public void createTicket_returnsBadRequest_whenDescriptionIsTooLong() throws Exception {
        //arrange
        TicketRequest mockRequest = new TicketRequest();
        mockRequest.setTitle("Valid title");
        mockRequest.setDescription("x".repeat(251));

        //act
        ResultActions response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockRequest))
        );

        //assert
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("Description cannot be more than 250 characters"))
        ;

        verify(ticketService, never()).createTicket(any());
    }

    @Test
    public void createTicket_returnsBadRequest_whenPriorityIsNull() throws Exception {
        //arrange
        TicketRequest mockRequest = new TicketRequest();
        mockRequest.setTitle("Valid title");

        //act
        ResultActions response = mockMvc.perform(
                post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializer.writeValueAsString(mockRequest))
        );

        //assert
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.priority").value("Priority cannot be empty"))
        ;

        verify(ticketService, never()).createTicket(any());
    }

}
