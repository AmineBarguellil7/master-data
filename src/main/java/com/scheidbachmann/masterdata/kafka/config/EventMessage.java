package com.scheidbachmann.masterdata.kafka.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scheidbachmann.masterdata.enums.EventType;
import com.scheidbachmann.masterdata.kafka.Schema;

import static java.util.Objects.requireNonNull;

public class EventMessage<T extends Schema, E extends Message<T>> {

    private final E event;

    private EventMessage(final E event) {
        this.event = requireNonNull(event, "The event must not be null!");
    }

    /**
     * Creates a new event message that can be published.
     *
     * @param event the actual event to publish.
     * @param <T> the type of the payload.
     * @param <E> the type of the event.
     * @return a new event message that can be published.
     */
    public static <T extends Schema, E extends Message<T>> EventMessage<T, E> of(final E event) {
        return new EventMessage<>(event);
    }

    @JsonProperty("version")
    public long getVersion() {
        return 1L;
    }

    @JsonProperty("id")
    public String getId() {
        return event.getId();
    }

    @JsonProperty("revision")
    public long getRevision() {
        return event.getRevision();
    }

    @JsonProperty("type")
    public String getType() {
        return event.getEventType();
    }

    @JsonProperty("tenant")
    public String getTenantId() {
        return event.getTenantId();
    }

    @JsonProperty("payload")
    public T getPayload() {
        return event.getPayload();
    }
}
