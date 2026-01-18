package com.example.factory.dto;

public class TopDefectLineDTO {

    public String lineId;
    public long totalDefects;
    public long eventCount;
    public double defectsPercent;

    public TopDefectLineDTO(String lineId, long totalDefects, long eventCount, double defectsPercent) {
        this.lineId = lineId;
        this.totalDefects = totalDefects;
        this.eventCount = eventCount;
        this.defectsPercent = defectsPercent;
    }
}
