
/**
 * Created By Amine Barguellil
 * Date : 2/20/2024
 * Time : 2:15 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.entity.Contract;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ServiceDto {


  private Set<ContractDto> contracts;

  private String id;

  private String name;

  private String subName;

  private Set<ConnectionPointServiceDto> connectionPointServices;

}
