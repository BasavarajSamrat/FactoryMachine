package com.example.factory.repository;

import com.example.factory.model.MachineEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MachineEventRepository extends JpaRepository<MachineEvent, Long> {

    Optional<MachineEvent> findByEventId(String eventId);

    List<MachineEvent> findByMachineIdAndEventTimeBetween(
            String machineId, Instant start, Instant end);

    List<MachineEvent> findByFactoryIdAndEventTimeBetween(
            String factoryId,
            Instant from,
            Instant to
    );

}
