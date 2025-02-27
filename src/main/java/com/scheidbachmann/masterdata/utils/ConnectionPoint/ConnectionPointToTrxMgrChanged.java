///**
// * Created By Amine Barguellil
// * Date : 5/13/2024
// * Time : 10:18 AM
// * Project Name : master-data
// */
//
//
//package com.scheidbachmann.masterdata.utils.ConnectionPoint;
//
//import com.scheidbachmann.masterdata.dto.ConnectionPointToTrxMgr;
//import com.scheidbachmann.masterdata.kafka.config.Event;
//
//import java.util.Optional;
//
//import static java.util.Objects.requireNonNull;
//
//public class ConnectionPointToTrxMgrChanged implements Event<ConnectionPointToTrxMgr> {
//
//    public static String EVENT_TYPE = "CONNECTION_POINT_CHANGED";
//
//    private final ConnectionPointToTrxMgr connectionPointToTrxMgr;
//
//    private final String tenant;
//
//    public ConnectionPointToTrxMgrChanged(ConnectionPointToTrxMgr connectionPointToTrxMgr, String tenant) {
//        this.connectionPointToTrxMgr = requireNonNull(connectionPointToTrxMgr, "The connectionPoint must not be null.");
//        this.tenant = tenant;
//    }
//
//
//    /**
//     * Creates a new {@link ConnectionPointToTrxMgrChanged} object.
//     *
//     * @param connectionPointToTrxMgr the created {@link ConnectionPointToTrxMgr}.
//     * @return a new connection point created object.
//     */
//    public static ConnectionPointToTrxMgrChanged with(final ConnectionPointToTrxMgr connectionPointToTrxMgr , final String tenant ) {
//        return new ConnectionPointToTrxMgrChanged(connectionPointToTrxMgr, tenant);
//    }
//
//    @Override
//    public String getEventType() {
//        return EVENT_TYPE;
//    }
//
//    @Override
//    public Optional<String> getId() {
//        return Optional.empty();
//    }
//
//    @Override
//    public long getRevision() {
//        return 0;
//    }
//
//    @Override
//    public ConnectionPointToTrxMgr getPayload() {
//        return connectionPointToTrxMgr;
//    }
//
//    @Override
//    public String getTenantId() {
//        return tenant;
//    }
//
//
//
//}
