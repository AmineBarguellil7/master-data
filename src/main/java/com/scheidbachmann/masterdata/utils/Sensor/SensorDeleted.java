///**
// * Created By Amine Barguellil
// * Date : 5/4/2024
// * Time : 3:33 PM
// * Project Name : master-data
// */
//
//
//package com.scheidbachmann.masterdata.utils.Sensor;
//
//import com.scheidbachmann.masterdata.entity.Sensor;
//import com.scheidbachmann.masterdata.kafka.config.Event;
//
//import java.util.Optional;
//
//import static java.util.Objects.requireNonNull;
//
//public class SensorDeleted  implements Event<Sensor> {
//
//    public static String EVENT_TYPE = "SensorDeleted";
//
//    private final Sensor sensor;
//
//    private final String tenant;
//
//    private SensorDeleted(final Sensor sensor, final String tenant) {
//        this.sensor = requireNonNull(sensor, "The sensor must not be null.");
//        this.tenant = tenant;
//    }
//
//    public static SensorDeleted with(final Sensor sensor, final String tenant) {
//        return new SensorDeleted(sensor, tenant);
//    }
//
//
//    @Override
//    public String getEventType() {
//        return EVENT_TYPE;
//    }
//
//    @Override
//    public Optional<String> getId() {
//        return Optional.ofNullable(sensor.getId());
//    }
//
//    @Override
//    public long getRevision() {
//        return sensor.getRevision();
//    }
//
//    @Override
//    public Sensor getPayload() {
//        return sensor;
//    }
//
//    @Override
//    public String getTenantId() {
//        return tenant;
//    }
//
//}
