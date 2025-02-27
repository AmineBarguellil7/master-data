///**
// * Created By Amine Barguellil
// * Date : 5/4/2024
// * Time : 3:32 PM
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
//public class SensorChanged  implements Event<Sensor> {
//
//    public static String EVENT_TYPE = "SensorChanged";
//
//    private final Sensor sensor;
//    private final String tenant;
//
//    private SensorChanged(final Sensor sensor, final String tenant) {
//        this.sensor = requireNonNull(sensor, "The sensor must not be null.");
//        this.tenant = tenant;
//    }
//
//    /**
//     * Creates a new {@link SensorChanged} object.
//     *
//     * @param sensor the changed {@link Sensor}.
//     * @return a new sensor changed object.
//     */
//    public static SensorChanged with(final Sensor sensor, final String tenant) {
//        return new SensorChanged(sensor, tenant);
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
