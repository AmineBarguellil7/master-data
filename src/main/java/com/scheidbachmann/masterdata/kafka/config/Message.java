package com.scheidbachmann.masterdata.kafka.config;

import com.scheidbachmann.masterdata.enums.EventType;
import com.scheidbachmann.masterdata.kafka.Schema;

public interface Message<T extends Schema> {
    String getTenantId();
    long getRevision();
    String getId();
    T getPayload();

    String getEventType();
}
