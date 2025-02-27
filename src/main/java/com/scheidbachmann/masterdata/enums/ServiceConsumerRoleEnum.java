package com.scheidbachmann.masterdata.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author KaouechHaythem
 * role of service usage at connection end point
 */
public enum ServiceConsumerRoleEnum {

  CONSUMER("CONSUMER"),

  SUPPLIER("SUPPLIER"),

  NONE("NONE");

  private String value;

  ServiceConsumerRoleEnum(String value) {
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
  public static ServiceConsumerRoleEnum fromValue(String value) {
    for (ServiceConsumerRoleEnum b : ServiceConsumerRoleEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
