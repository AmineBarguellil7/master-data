package com.scheidbachmann.masterdata.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-02-07T08:55:33.999185200-08:00[America/Los_Angeles]")
public enum BusinessPartnerType {

  B2C("B2C"),

  DEFAULT("DEFAULT");

  private String value;

  BusinessPartnerType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static BusinessPartnerType fromValue(String value) {
    for (BusinessPartnerType b : BusinessPartnerType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }

}
