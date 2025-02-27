package com.scheidbachmann.masterdata.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * @author KaouechHaythem
 */
@Embeddable
@Data
public class ConnectivityCredentialsOauth {

  private String tokenEndpoint = null;

  private String clientId = null;

  private String clientCredentials = null;
}
