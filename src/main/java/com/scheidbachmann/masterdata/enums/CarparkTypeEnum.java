package com.scheidbachmann.masterdata.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CarparkTypeEnum {
  _UNSPECIFIED_("_UNSPECIFIED_"),

  MULTI_STOREY_CARPARK("MULTI_STOREY_CARPARK"),

  UNDERGROUND_CARPARK("UNDERGROUND_CARPARK"),

  PARKING_AREA("PARKING_AREA");
  private String value;

  CarparkTypeEnum(String value) {
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
  public static CarparkTypeEnum fromValue(String value) {
    for (CarparkTypeEnum b : CarparkTypeEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }



}
