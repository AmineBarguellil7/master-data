///**
// * Created By Amine Barguellil
// * Date : 5/4/2024
// * Time : 3:42 PM
// * Project Name : master-data
// */
//
//
//package com.scheidbachmann.masterdata.utils.Contract;
//
//import com.scheidbachmann.masterdata.entity.Contract;
//import com.scheidbachmann.masterdata.kafka.config.Event;
//
//import java.util.Optional;
//
//import static java.util.Objects.requireNonNull;
//
//public class ContractChanged implements Event<Contract> {
//
//    public static String EVENT_TYPE = "ContractChanged";
//
//    private final Contract contract;
//
//    private final String tenant;
//
//    private ContractChanged(final Contract contract, String tenant) {
//        this.contract = requireNonNull(contract, "The contract must not be null.");
//        this.tenant = tenant;
//    }
//
//    /**
//     * Creates a new {@link ContractChanged} object.
//     *
//     * @param contract the changed {@link Contract}.
//     * @return a new contract changed object.
//     */
//    public static ContractChanged with(final Contract contract , final  String tenant) {
//        return new ContractChanged(contract, tenant);
//    }
//
//    @Override
//    public String getEventType() {
//        return EVENT_TYPE;
//    }
//
//    @Override
//    public Optional<String> getId() {
//        return Optional.ofNullable(contract.getId());
//    }
//
//    @Override
//    public long getRevision() {
//        return contract.getRevision();
//    }
//
//    @Override
//    public Contract getPayload() {
//        return contract;
//    }
//
//    @Override
//    public String getTenantId() {
//        return tenant;
//    }
//}
//
//
