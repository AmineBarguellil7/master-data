package com.scheidbachmann.masterdata.kafka;


/**
 * @author KaouechHaythem
 */
public interface KafkaConsumer {
  void onMessage(String message);
}
