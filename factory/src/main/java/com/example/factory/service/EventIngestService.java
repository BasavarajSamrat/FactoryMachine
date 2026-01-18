package com.example.factory.service;

import com.example.factory.dto.BatchResponseDTO;
import com.example.factory.dto.EventRequestDTO;
import com.example.factory.model.MachineEvent;

import java.time.Instant;
import java.util.List;

public interface EventIngestService {

    public BatchResponseDTO ingest(List<EventRequestDTO> events);

    public boolean isSamePayload(MachineEvent e, EventRequestDTO d);

    public void update(MachineEvent e, EventRequestDTO dto, Instant now);

    public MachineEvent toEntity(EventRequestDTO dto, Instant now);
}
