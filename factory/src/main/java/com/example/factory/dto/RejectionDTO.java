package com.example.factory.dto;

public class RejectionDTO {
    public String eventId;
    public String reason;

    public RejectionDTO(String eventId, String reason) {
        this.eventId = eventId;
        this.reason = reason;
    }
}
