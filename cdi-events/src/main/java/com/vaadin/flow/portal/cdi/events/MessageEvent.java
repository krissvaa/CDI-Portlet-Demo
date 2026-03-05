package com.vaadin.flow.portal.cdi.events;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * CDI event payload carrying a message and its priority level.
 */
public class MessageEvent implements Serializable {

    private final String message;
    private final String priority;
    private final LocalTime timestamp;

    public MessageEvent(String message, String priority) {
        this.message = message;
        this.priority = priority;
        this.timestamp = LocalTime.now();
    }

    public String getMessage() {
        return message;
    }

    public String getPriority() {
        return priority;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }
}
