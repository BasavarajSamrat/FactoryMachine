package com.example.factory.service;

import com.example.factory.dto.*;
import com.example.factory.model.MachineEvent;

import com.example.factory.repository.MachineEventRepository;
import com.example.factory.utils.ValidationUtil;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EventIngestServiceImpl implements EventIngestService {

    private final MachineEventRepository machineEventRepository;

    public EventIngestServiceImpl(MachineEventRepository machineEventRepository) {
        this.machineEventRepository = machineEventRepository;
    }

    @Transactional
    public BatchResponseDTO ingest(List<EventRequestDTO> events) {

        BatchResponseDTO response = new BatchResponseDTO();

        for (EventRequestDTO dto : events) {

            if (ValidationUtil.isInvalidDuration(dto.durationMs)) {
                response.rejected++;
                response.rejections.add(new RejectionDTO(dto.eventId, "INVALID_DURATION"));
                continue;
            }

            if (ValidationUtil.isFutureEvent(dto.eventTime)) {
                response.rejected++;
                response.rejections.add(new RejectionDTO(dto.eventId, "FUTURE_EVENT"));
                continue;
            }

            Instant now = Instant.now();

            var existingOpt = machineEventRepository.findByEventId(dto.eventId);

            if (existingOpt.isEmpty()) {
                try {
                    machineEventRepository.save(toEntity(dto, now));
                    response.accepted++;
                } catch (DataIntegrityViolationException ex) {
                    response.deduped++;
                }
                continue;
            }

            MachineEvent existing = existingOpt.get();

            if (isSamePayload(existing, dto)) {
                response.deduped++;
                continue;
            }

            if (now.isAfter(existing.getReceivedTime())) {
                update(existing, dto, now);
                response.updated++;
            }
        }

        return response;
    }

    public boolean isSamePayload(MachineEvent e, EventRequestDTO d) {
        return e.getDurationMs().equals(d.durationMs)
                && e.getDefectCount().equals(d.defectCount)
                && e.getEventTime().equals(d.eventTime);
    }



    public void update(MachineEvent e, EventRequestDTO dto, Instant now) {
        e.setEventTime(dto.eventTime);
        e.setDurationMs(dto.durationMs);
        e.setDefectCount(dto.defectCount);
        e.setReceivedTime(now);
        e.setFactoryId(dto.factoryId);
        e.setLineId(dto.lineId);

    }

    public MachineEvent toEntity(EventRequestDTO dto, Instant now) {
        MachineEvent e = new MachineEvent();
        e.setEventId(dto.eventId);
        e.setEventTime(dto.eventTime);
        e.setMachineId(dto.machineId);
        e.setFactoryId(dto.factoryId);
        e.setLineId(dto.lineId);
        e.setDurationMs(dto.durationMs);
        e.setDefectCount(dto.defectCount);
        e.setReceivedTime(now);
        return e;
    }

}
