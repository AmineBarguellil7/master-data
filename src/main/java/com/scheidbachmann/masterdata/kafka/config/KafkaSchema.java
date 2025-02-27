/**
 * Created By Amine Barguellil
 * Date : 5/22/2024
 * Time : 9:21 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.kafka.config;

import com.scheidbachmann.masterdata.enums.EventType;
import com.scheidbachmann.masterdata.kafka.Schema;

import java.util.Objects;

public class KafkaSchema<T extends Schema> implements Message<T> {

    private final T info;
    private final String tenantId;
    private final String eventType;

    private KafkaSchema(final T info, final String tenantId, final String eventType) {
        this.info = Objects.requireNonNull(info, "The businessPartner must not be null.");
        this.tenantId = tenantId;
        this.eventType = eventType;
    }

    public static <T extends Schema> KafkaSchema<T> with(final T info, final String tenantId, final String eventType) {
        return new KafkaSchema<>(info, tenantId, eventType);
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getId() {
        return info.getId();
    }

    @Override
    public long getRevision() {
        return info.getRevision();
    }

    @Override
    public T getPayload() {
        return info;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }
}
