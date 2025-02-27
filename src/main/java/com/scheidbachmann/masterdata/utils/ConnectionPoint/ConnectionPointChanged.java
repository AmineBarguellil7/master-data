///**
// * Created By Amine Barguellil
// * Date : 5/4/2024
// * Time : 1:45 PM
// * Project Name : master-data
// */
//
//
//package com.scheidbachmann.masterdata.utils.ConnectionPoint;
//
//import com.scheidbachmann.masterdata.entity.ConnectionPoint;
//import com.scheidbachmann.masterdata.kafka.config.Event;
//
//import java.util.Optional;
//
//import static java.util.Objects.requireNonNull;
//
//public class ConnectionPointChanged implements Event<ConnectionPoint> {
//
//    public static String EVENT_TYPE = "ConnectionPointChanged";
//
//    private final ConnectionPoint connectionPoint;
//
//    private final String tenantName;
//
//    private ConnectionPointChanged(final ConnectionPoint connectionPoint, String tenantName) {
//        this.connectionPoint = requireNonNull(connectionPoint, "The connectionPoint must not be null.");
//        this.tenantName = tenantName;
//    }
//
//    /**
//     * Creates a new {@link ConnectionPointChanged} object.
//     *
//     * @param connectionPoint the changed {@link ConnectionPoint}.
//     * @return a new connection point changed object.
//     */
//    public static ConnectionPointChanged with(final ConnectionPoint connectionPoint , final String tenantName) {
//        return new ConnectionPointChanged(connectionPoint, tenantName);
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
