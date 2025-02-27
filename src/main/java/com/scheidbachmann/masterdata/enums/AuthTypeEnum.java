package com.scheidbachmann.masterdata.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author KaouechHaythem
 * type of authorization for a service
 */

public enum AuthTypeEnum {

  _UNSPECIFIED_("_UNSPECIFIED_"),

  BASIC_AUTH("BASIC_AUTH"),

  API_KEY("API_KEY"),

  OAUTH_AUTHCODE("OAUTH_AUTHCODE");

  private String value;

  AuthTypeEnum(String value) {
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
  public static AuthTypeEnum fromValue(String value) {
    for (AuthTypeEnum b : AuthTypeEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
