package dev.lukasdo.ticketingapi.dto;

import dev.lukasdo.ticketingapi.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketRequest {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 50, message = "Title cannot be more than 50 characers")
    private String title;
    @Size(max = 250, message = "Description cannot be more than 250 characters")
    private String description;
    @NotNull(message = "Priority cannot be empty")
    private Priority priority;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
