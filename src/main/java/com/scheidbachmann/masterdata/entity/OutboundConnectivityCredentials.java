package com.scheidbachmann.masterdata.entity;

import com.scheidbachmann.masterdata.enums.AuthTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author KaouechHaythem
 * credentials for an external service, additional mandatory fields depend on the authType chosen (e.g. userName and passswordCredentials for basicAuth)
 */
@Embeddable
@Data
public class OutboundConnectivityCredentials {

  private String userName = null;

  private AuthTypeEnum authType = null;

  @Column(name = "pwd_credentials")
  private String passwordCredentials = null;

  private String apiKey = null;

  @AttributeOverrides({
    @AttributeOverride(name = "tokenEndpoint", column = @Column(name = "OACC_ENDPOINT")),
    @AttributeOverride(name = "clientId", column = @Column(name = "OACC_CLIENT_ID")),
    @AttributeOverride(name = "clientCredentials", column = @Column(name = "OACC_CLIENT_CREDENTIALS"))
  })
  @Embedded
  private ConnectivityCredentialsOauth oauthCredentials = null;
}
