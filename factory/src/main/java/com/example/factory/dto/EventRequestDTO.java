package com.example.factory.dto;

import java.time.Instant;

public class EventRequestDTO {

    public String eventId;
    public Instant eventTime;
    public String machineId;
    public String factoryId;
    public String lineId;
    public Long durationMs;
    public Integer defectCount;
}
