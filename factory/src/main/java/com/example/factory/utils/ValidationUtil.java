package com.example.factory.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ValidationUtil {

    public static final long MAX_DURATION = 6 * 60 * 60 * 1000;

    public static boolean isInvalidDuration(Long duration) {
        return duration < 0 || duration > MAX_DURATION;
    }

    public static boolean isFutureEvent(Instant eventTime) {
        return eventTime.isAfter(Instant.now().plus(15, ChronoUnit.MINUTES));
    }
}
