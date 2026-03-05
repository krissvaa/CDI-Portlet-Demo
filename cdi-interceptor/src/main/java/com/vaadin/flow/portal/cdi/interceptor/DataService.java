package com.vaadin.flow.portal.cdi.interceptor;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Application-scoped service with {@link Logged}-annotated methods.
 * The {@link LoggingInterceptor} transparently intercepts every call,
 * logging entry/exit/duration/errors without any code changes here.
 */
@ApplicationScoped
public class DataService implements Serializable {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private final Random random = new Random();

    /**
     * Simulates fetching data with a short delay.
     */
    @Logged
    public String fetchData() {
        simulateDelay(50, 200);
        return "Data fetched at " + LocalTime.now().format(TIME_FMT);
    }

    /**
     * Simulates processing multiple items with a longer delay.
     */
    @Logged
    public List<String> processItems(int count) {
        simulateDelay(100, 400);
        List<String> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add("Item-" + (i + 1));
        }
        return items;
    }

    /**
     * Simulates an operation that randomly fails, demonstrating how
     * the interceptor handles exceptions.
     */
    @Logged
    public String riskyOperation() {
        simulateDelay(20, 100);
        if (random.nextBoolean()) {
            throw new RuntimeException("Simulated failure");
        }
        return "Operation succeeded at " + LocalTime.now().format(TIME_FMT);
    }

    private void simulateDelay(int minMs, int maxMs) {
        try {
            Thread.sleep(minMs + random.nextInt(maxMs - minMs));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
