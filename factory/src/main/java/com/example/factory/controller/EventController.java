package com.example.factory.controller;

import com.example.factory.dto.*;
import com.example.factory.service.EventIngestServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    private final EventIngestServiceImpl eventIngestService;

    public EventController(EventIngestServiceImpl service) {
        this.eventIngestService = service;
    }

    @PostMapping("/events/batch")
    public BatchResponseDTO ingest(@RequestBody List<EventRequestDTO> events) {
        return eventIngestService.ingest(events);
    }
}
