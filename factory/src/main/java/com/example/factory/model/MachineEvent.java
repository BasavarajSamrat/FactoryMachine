package com.example.factory.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(
        name = "machine_events",
        uniqueConstraints = @UniqueConstraint(columnNames = "eventId"),
        indexes = {
                @Index(name = "idx_event_id", columnList = "eventId"),
                @Index(name = "idx_machine_time", columnList = "machineId,eventTime")
        }
)

@Data
public class MachineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
    private Instant eventTime;
    private Instant receivedTime;
    private String machineId;
    private Long durationMs;
    private Integer defectCount;
    private String factoryId;
    private String lineId;






}
