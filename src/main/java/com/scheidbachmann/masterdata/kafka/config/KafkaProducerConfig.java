package com.scheidbachmann.masterdata.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KaouechHaythem
 */
@Configuration
public class KafkaProducerConfig {

  @Value(value = "${kafka.broker}")
  private String kafkaBrokers;


  /**
   * Makes Apache Kafka client aware of Spring configuration
   *
   * @param objectMapper
   * @return
   */
  @Bean
  public ProducerFactory<String, Object> producerFactory( ObjectMapper objectMapper) {
    Map<String, Object> configProps = new HashMap<>();;
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "20971520");
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    JsonSerializer<Object> jsonSerializer = new JsonSerializer<>(objectMapper);
    jsonSerializer.configure(configProps, false);
    return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), jsonSerializer);
  }

  @Bean
  public KafkaTemplate kafkaTemplate( ObjectMapper objectMapper) {
    return new KafkaTemplate<>(producerFactory( objectMapper));
  }


}
