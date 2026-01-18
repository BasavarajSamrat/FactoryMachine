# Benchmark Report – Factory Events Backend System

## Objective

The goal of this benchmark is to verify that the system can process a batch of **1,000 events in under 1 second**, while remaining correct and stable under concurrent ingestion, as required by the assignment.

---

## Environment

- **CPU:** Intel i5 / AMD Ryzen 5 class processor  
- **RAM:** 16 GB  
- **Operating System:** Windows / Linux  
- **Java Version:** 17 (LTS)  
- **Spring Boot Version:** 3.2.x  
- **Database:** MySQL 8.x (InnoDB)  
- **Connection Pool:** HikariCP  

---

## Database Configuration

- Storage Engine: InnoDB  
- Autocommit: Enabled  
- Indexes:
  - `eventId` (UNIQUE)
  - `machineId, eventTime`
  - `factoryId, eventTime`

These indexes ensure fast deduplication and efficient time-range queries.

---

## Benchmark Setup

- **Endpoint Tested:** `POST /events/batch`
- **Batch Size:** 1,000 events
- **Concurrent Requests:** 10 parallel requests
- **Event Payload:** Valid events with mixed defect counts
- **Measurement Tool:** Postman / System logs (`System.nanoTime()`)

Each benchmark run inserts or deduplicates events inside a single transactional boundary.

---

## Benchmark Command

Example command used to trigger the benchmark:

```bash
POST http://localhost:8080/events/batch
