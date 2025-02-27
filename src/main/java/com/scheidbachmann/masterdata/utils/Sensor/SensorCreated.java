///**
// * Created By Amine Barguellil
// * Date : 5/4/2024
// * Time : 3:29 PM
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
//public class SensorCreated  implements Event<Sensor> {
//
//
//    public static String EVENT_TYPE = "SensorCreated";
//
//    private final Sensor sensor;
//    private final String tenant;
//
//    private SensorCreated(final Sensor sensor, final String tenant) {
//        this.sensor = requireNonNull(sensor, "The sensor must not be null.");
//        this.tenant = tenant;
//    }
//
//    /**
//     * Creates a new {@link SensorCreated} object.
//     *
//     * @param sensor the created {@link Sensor}.
//     * @return a new sensor created object.
//     */
//    public static SensorCreated with(final Sensor sensor, final String tenant) {
//        return new SensorCreated(sensor, tenant);
//    }
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
//}
