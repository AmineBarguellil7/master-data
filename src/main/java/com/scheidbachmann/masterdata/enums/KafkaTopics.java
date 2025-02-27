package com.scheidbachmann.masterdata.enums;

public enum KafkaTopics {
    BUSINESS_PARTNER_TOPIC("BusinessPartner.BusinessPartner.1.Topic"),
    CONNECTION_POINT_TOPIC("ConnectionPoint.ConnectionPoint.1.Topic"),
    SENSOR_TOPIC("Sensor.Sensor.1.Topic"),
    CONTRACT_TOPIC("Contract.Contract.1.Topic");

    private final String value;

    KafkaTopics(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public String getValue() {
        return value;
    }

}
