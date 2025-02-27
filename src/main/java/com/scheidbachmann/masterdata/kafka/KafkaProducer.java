package com.scheidbachmann.masterdata.kafka;

import com.scheidbachmann.masterdata.enums.EventType;
import com.scheidbachmann.masterdata.enums.KafkaTopics;
import com.scheidbachmann.masterdata.kafka.config.EventMessage;

public interface KafkaProducer {
    void produceMessage(KafkaTopics topic, EventMessage eventMessage);
}
