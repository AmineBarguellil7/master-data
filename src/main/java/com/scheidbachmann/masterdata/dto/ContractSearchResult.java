package com.scheidbachmann.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.PriorityLevel;
import com.scheidbachmann.masterdata.enums.SelectionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author KaouechHaythem
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractSearchResult {
  private String id;

  private String supplierId;

  private String supplierName;

  private String consumerId;

  private String consumerName;

  private String serviceName;

  private LocalDate contractStart;

  private LocalDate contractEnd;



}
