///**
// * Created By Amine Barguellil
// * Date : 5/3/2024
// * Time : 3:56 PM
// * Project Name : master-data
// */
//
//
//package com.scheidbachmann.masterdata.utils.BusinessPartner;
//
//
//
//import com.scheidbachmann.masterdata.entity.BusinessPartner;
//import com.scheidbachmann.masterdata.kafka.config.Event;
//
//import java.util.Optional;
//
//import static java.util.Objects.requireNonNull;
//
//public class BusinessPartnerDeleted implements Event<BusinessPartner> {
//
//    public static String EVENT_TYPE = "BusinessPartnerDeleted";
//
//    private final BusinessPartner businessPartner;
//    private final String tenantId;
//
//    private BusinessPartnerDeleted(final BusinessPartner businessPartner, final String tenantId) {
//        this.businessPartner = requireNonNull(businessPartner, "The businessPartner must not be null.");
//        this.tenantId = tenantId;
//    }
//
//    /**
//     * Creates a new {@link BusinessPartnerCreated} object.
//     *
//     * @param businessPartner the created {@link BusinessPartner}.
//     * @return a new business partner created object.
//     */
//    public static BusinessPartnerDeleted with(final BusinessPartner businessPartner, String tenantId) {
//        return new BusinessPartnerDeleted(businessPartner, tenantId);
//    }
//
//    @Override
//    public String getEventType() {
//        return EVENT_TYPE;
//    }
//
//    @Override
//    public Optional<String> getId() {
//        return Optional.ofNullable(businessPartner.getId());
//    }
//
//    @Override
//    public long getRevision() {
//        return businessPartner.getRevision();
//    }
//
//    @Override
//    public BusinessPartner getPayload() {
//        return businessPartner;
//    }
//
//    @Override
//    public String getTenantId() {
//        return tenantId;
//    }
//}
