package com.scheidbachmann.masterdata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheidbachmann.masterdata.enums.AuthTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class OutboundConnectivityCredentialsDto {

  @Size(max = 100)
  private String userName;

  @Size(max = 20)
  private AuthTypeEnum authType;

  @Size(max = 100)
  private String passwordCredentials;

  @Size(max = 128)
  private String apiKey;

  private ConnectivityCredentialsOauthDto oauthCredentials;
}
