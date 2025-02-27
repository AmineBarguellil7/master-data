/**
 * Created By Amine Barguellil
 * Date : 5/30/2024
 * Time : 3:16 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.CarparkTypeEnum;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionPointExport {
    private String id;

    private Long revision;


    @NotNull
    private ConnectionPointTypeEnum type;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String locationId;

    @Size(max = 10)
    private String facilityId;

    @Size(max = 10)
    private String cellId;

    @Size(max = 10)
    private String operatorId;

    private CarparkTypeEnum carparkType;

    private LocalDateTime lastModified;

    private String orderNumber;

    private String technicalPlace;

    private LocalDate activatedAt;

    private String other;

    private boolean withLeaveLoop;

    private String tenantName;

    private String geometryPath;

    private boolean keycloakInboundUser;

    private String businessPartnerId;


}
