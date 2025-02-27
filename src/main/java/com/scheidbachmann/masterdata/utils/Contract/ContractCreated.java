///**
// * Created By Amine Barguellil
// * Date : 5/4/2024
// * Time : 3:40 PM
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
//public class ContractCreated implements Event<Contract> {
//
//    public static String EVENT_TYPE = "ContractCreated";
//
//    private final Contract contract;
//
//    private final String tenant;
//
//    private ContractCreated(final Contract contract, String tenant) {
//        this.contract = requireNonNull(contract, "The contract must not be null.");
//        this.tenant = tenant;
//    }
//
//    /**
//     * Creates a new {@link ContractCreated} object.
//     *
//     * @param contract the created {@link Contract}.
//     * @return a new contract created object.
//     */
//    public static ContractCreated with(final Contract contract ,final String tenant) {
//        return new ContractCreated(contract, tenant);
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
