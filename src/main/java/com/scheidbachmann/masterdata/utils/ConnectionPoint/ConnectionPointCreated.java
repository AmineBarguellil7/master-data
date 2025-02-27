///**
// * Created By Amine Barguellil
// * Date : 5/4/2024
// * Time : 1:34 PM
// * Project Name : master-data
// */
//
//
//package com.scheidbachmann.masterdata.utils.ConnectionPoint;
//
//
//import com.scheidbachmann.masterdata.entity.ConnectionPoint;
//import com.scheidbachmann.masterdata.kafka.config.Event;
//
//import java.util.Optional;
//
//import static java.util.Objects.requireNonNull;
//
//public class ConnectionPointCreated implements Event<ConnectionPoint> {
//
//    public static String EVENT_TYPE = "ConnectionPointCreated";
//
//    private final ConnectionPoint connectionPoint;
//
//    private final String tenantName;
//
//    private ConnectionPointCreated(final ConnectionPoint connectionPoint, String tenantName) {
//        this.connectionPoint = requireNonNull(connectionPoint, "The connectionPoint must not be null.");
//        this.tenantName = tenantName;
//    }
//
//    /**
//     * Creates a new {@link ConnectionPointCreated} object.
//     *
//     * @param connectionPoint the created {@link ConnectionPoint}.
//     * @return a new connection point created object.
//     */
//    public static ConnectionPointCreated with(final ConnectionPoint connectionPoint , final String tenantName ) {
//        return new ConnectionPointCreated(connectionPoint, tenantName);
//    }
//
//    @Override
//    public String getEventType() {
//        return EVENT_TYPE;
//    }
//
//    @Override
//    public Optional<String> getId() {
//        return Optional.ofNullable(connectionPoint.getId());
//    }
//
//    @Override
//    public long getRevision() {
//        return connectionPoint.getRevision();
//    }
//
//    @Override
//    public ConnectionPoint getPayload() {
//        return connectionPoint;
//    }
//
//    @Override
//    public String getTenantId() {
//        return tenantName;
//    }
//
//}
