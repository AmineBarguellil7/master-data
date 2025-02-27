/**
 * Created By Amine Barguellil
 * Date : 5/22/2024
 * Time : 11:01 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.kafka.config.Contract;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.dto.BusinessPartnerDto;
import com.scheidbachmann.masterdata.dto.ConnectionPointDto;
import com.scheidbachmann.masterdata.dto.ContractLicenseDto;
import com.scheidbachmann.masterdata.dto.ServiceDto;
import com.scheidbachmann.masterdata.enums.PriorityLevel;
import com.scheidbachmann.masterdata.enums.SelectionType;
import com.scheidbachmann.masterdata.kafka.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractSchema implements Schema  {



    private String id;

    @NotNull
    private Long revision;

    private ContractLicenseDto supplierLicense;

    private ContractLicenseDto consumerLicense;

    @NotNull(message = "priorityLevel is required")
    private PriorityLevel priorityLevel;

    @NotNull(message = "supplierConnectionPointSelection is required")
    private SelectionType supplierConnectionPointSelection;

    @NotNull(message = "consumerConnectionPointSelection is required")
    private SelectionType consumerConnectionPointSelection;

    @NotNull(message = "contractStart is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate contractStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate contractEnd;

    @NotBlank(message = "serviceName is required")
    private String serviceName;

    @NotNull
    private String serviceId;

    @NotNull
    private String consumerId;

    @NotNull
    private String supplierId;


    //  @NotNull
    private ServiceDto service;

    //  @NotNull
    private BusinessPartnerDto consumer;

    //  @NotNull
    private BusinessPartnerDto supplier;

    private Set<ConnectionPointDto> consumerConnectionPoints = new HashSet<>();
    private Set<String> consumerConnectionPointsIds;

    private Set<ConnectionPointDto> supplierConnectionPoints = new HashSet<>();
    private Set<String> supplierConnectionPointsIds;






    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getRevision() {
        return revision;
    }
}
