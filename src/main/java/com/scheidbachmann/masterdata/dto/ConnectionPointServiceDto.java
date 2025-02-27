package com.scheidbachmann.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.ServiceConsumerRoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionPointServiceDto {

  private String id;

  private Long revision;

  private String serviceName;

  private ServiceConsumerRoleEnum endpointRole;

  @NotNull
  private String connectionPointId;

  @NotNull
  private String serviceId;

  private ConnectionPointDto connectionPoint;

  private ServiceDto service;

}
