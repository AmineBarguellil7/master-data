package com.scheidbachmann.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectivityCredentialsOauthDto {

  @Size(max = 255)
  private String tokenEndpoint;

  @Size(max = 64)
  private String clientId;

  @Size(max = 128)
  private String clientCredentials;
}
