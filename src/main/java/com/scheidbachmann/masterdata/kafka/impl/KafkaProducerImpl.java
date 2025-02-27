package com.scheidbachmann.masterdata.kafka.impl;

import com.scheidbachmann.masterdata.enums.KafkaTopics;
import com.scheidbachmann.masterdata.kafka.KafkaProducer;
import com.scheidbachmann.masterdata.kafka.config.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerImpl implements KafkaProducer {

  private final KafkaTemplate<String, EventMessage> kafkaTemplate;

  public KafkaProducerImpl(KafkaTemplate<String, EventMessage> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void produceMessage(KafkaTopics topic, EventMessage eventMessage) {
    org.springframework.messaging.Message<EventMessage> message = MessageBuilder
            .withPayload(eventMessage)
            .setHeader(KafkaHeaders.TOPIC, topic.toString())
            .build();

    kafkaTemplate.send(message);
    kafkaTemplate.flush();
    log.info("Message sent successfully to topic: {}", topic);
  }
}