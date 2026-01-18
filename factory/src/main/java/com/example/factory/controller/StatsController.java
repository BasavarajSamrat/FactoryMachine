package com.example.factory.controller;

import com.example.factory.dto.TopDefectLineDTO;
import com.example.factory.model.MachineEvent;
import com.example.factory.repository.MachineEventRepository;
import com.example.factory.service.StatsServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final MachineEventRepository machineEventRepository;

    private final StatsServiceImpl statsService;

    public StatsController(MachineEventRepository repository, StatsServiceImpl statsService) {
        this.machineEventRepository = repository;
        this.statsService = statsService;
    }

    @GetMapping
    public Object stats(@RequestParam String machineId,@RequestParam Instant start, @RequestParam Instant end) {

        List<MachineEvent> events = machineEventRepository.findByMachineIdAndEventTimeBetween(machineId, start, end);

        long defects = events.stream()
                .filter(e -> e.getDefectCount() != -1)
                .mapToLong(MachineEvent::getDefectCount)
                .sum();

        double hours = (end.toEpochMilli() - start.toEpochMilli()) / 3600000.0;
        double rate = defects / hours;

        return new Object() {
            public long eventsCount = events.size();
            public long defectsCount = defects;
            public double avgDefectRate = rate;
            public String status = rate < 2.0 ? "Healthy" : "Warning";
        };
    }

    @GetMapping("/top-defect-lines")
    public List<TopDefectLineDTO> topDefectLines(
            @RequestParam String factoryId,
            @RequestParam Instant from,
            @RequestParam Instant to,
            @RequestParam(defaultValue = "10") int limit) {

        return statsService.topDefectLines(factoryId, from, to, limit);
    }

}
