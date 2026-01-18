package com.example.factory.service;

import com.example.factory.dto.TopDefectLineDTO;
import com.example.factory.model.MachineEvent;

import com.example.factory.repository.MachineEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {

    private final MachineEventRepository machineEventRepository;

    public StatsServiceImpl(MachineEventRepository machineEventRepository) {
        this.machineEventRepository = machineEventRepository;
    }

    public List<TopDefectLineDTO> topDefectLines(String factoryId, Instant from, Instant to, int limit) {

        List<MachineEvent> events = machineEventRepository.findByFactoryIdAndEventTimeBetween(factoryId, from, to);

        Map<String, List<MachineEvent>> byLine = events.stream().collect(Collectors.groupingBy(MachineEvent::getLineId));

        List<TopDefectLineDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<MachineEvent>> entry : byLine.entrySet()) {

            String lineId = entry.getKey();
            List<MachineEvent> lineEvents = entry.getValue();

            long eventCount = lineEvents.size();

            long totalDefects = lineEvents.stream()
                    .filter(e -> e.getDefectCount() != -1)
                    .mapToLong(MachineEvent::getDefectCount)
                    .sum();

            double defectsPercent = eventCount == 0 ? 0.0 : Math.round((totalDefects * 10000.0 / eventCount)) / 100.0;

            result.add(new TopDefectLineDTO(
                    lineId,
                    totalDefects,
                    eventCount,
                    defectsPercent
            ));
        }

        return result.stream()
                .sorted(Comparator.comparingLong((TopDefectLineDTO d) -> d.totalDefects).reversed())
                .limit(limit)
                .toList();
    }
}
