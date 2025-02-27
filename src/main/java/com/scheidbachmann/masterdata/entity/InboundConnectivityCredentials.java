package com.scheidbachmann.masterdata.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * @author KaouechHaythem
 * Credentials for an inbound request to entervo.connect (from cell computers or from external partners accessing the external entervo.connect API). At the moment only userName and apiKey (in case of external accessible connection point) are stored for a  Api-Key and e.g. userName and passsworrdCredentials for basicAuth)
 */
@Embeddable
@Data
public class InboundConnectivityCredentials {

  private String inbUserName = null;

  private String inbRateProfile = null;
}
