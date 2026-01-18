package com.example.factory;

import com.example.factory.dto.EventRequestDTO;
import com.example.factory.repository.MachineEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventConcurrencyTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MachineEventRepository repository;

    @Test
    void concurrentIngestion_sameEventId_shouldRemainConsistent() throws Exception {

        repository.deleteAll();

        EventRequestDTO event = new EventRequestDTO();
        event.eventId = "E-CONCURRENT";
        event.eventTime = Instant.parse("2026-01-15T10:00:00Z");
        event.machineId = "M-001";
        event.factoryId = "F01";
        event.lineId = "L-01";
        event.durationMs = 1000L;
        event.defectCount = 1;

        List<EventRequestDTO> payload = List.of(event);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        Callable<ResponseEntity<String>> task = () ->
                restTemplate.postForEntity("/events/batch", payload, String.class);

        int threads = 10;
        List<Future<ResponseEntity<String>>> results =
                executor.invokeAll(java.util.Collections.nCopies(threads, task));

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Only ONE row should exist
        assertThat(repository.count()).isEqualTo(1);
    }
}
