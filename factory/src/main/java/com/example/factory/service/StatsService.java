package com.example.factory.service;

import com.example.factory.dto.TopDefectLineDTO;

import java.time.Instant;
import java.util.List;

public interface StatsService {

    public List<TopDefectLineDTO> topDefectLines(String factoryId, Instant from, Instant to, int limit);
}
