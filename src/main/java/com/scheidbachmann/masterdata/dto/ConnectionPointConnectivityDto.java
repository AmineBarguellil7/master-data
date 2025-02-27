package com.scheidbachmann.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KaouechHaythem
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionPointConnectivityDto {

  private String id;

  private Long revision;


  private Integer port;

  @Size(max = 100)
  private String host;

  @Size(max = 200)
  private String baseUrl;

  @Size(max = 100)
  private String requestFormat;

  @Size(max = 100)
  private String responseFormat;

  private OutboundConnectivityCredentialsDto outboundCredentials;

  private InboundConnectivityCredentialsDto inboundCredentials;

  private String connectionPointId;

  private ConnectionPointDto connectionPoint;


}
