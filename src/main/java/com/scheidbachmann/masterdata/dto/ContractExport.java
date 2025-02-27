/**
 * Created By Amine Barguellil
 * Date : 5/30/2024
 * Time : 3:30 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.PriorityLevel;
import com.scheidbachmann.masterdata.enums.SelectionType;
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
public class ContractExport {
    private String id;

    @NotNull
    private Long revision;


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

}
