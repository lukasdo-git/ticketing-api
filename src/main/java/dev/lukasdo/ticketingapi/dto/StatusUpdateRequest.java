package dev.lukasdo.ticketingapi.dto;

import dev.lukasdo.ticketingapi.model.Status;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    @NotNull(message = "Status cannot be empty")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
