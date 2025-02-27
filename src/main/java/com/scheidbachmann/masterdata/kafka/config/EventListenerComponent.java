/**
 * Created By Amine Barguellil
 * Date : 5/21/2024
 * Time : 10:07 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.kafka.config;


import com.scheidbachmann.masterdata.enums.EventType;
import com.scheidbachmann.masterdata.enums.KafkaTopics;
import com.scheidbachmann.masterdata.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventListenerComponent<T extends Message<?>> {

    @Autowired
    private KafkaProducer eventMessageKafka;

    public void sendToTopic(KafkaTopics kafkaTopics, T info) {
        log.info("Sending event to Kafka TOPIC");

        EventMessage eventMessage1 = null;

        System.out.print("Class :"  + info.getEventType()+ "\n");


        EventMessage eventMessage =
            EventMessage.of(KafkaSchema.with(info.getPayload(), info.getTenantId(),info.getEventType()));

        eventMessage1=eventMessage;


        System.out.print(eventMessage1 + "/////////////////\n");

        eventMessageKafka.produceMessage(kafkaTopics, eventMessage1);
        log.info("EventMessage sent to Kafka");
    }
}